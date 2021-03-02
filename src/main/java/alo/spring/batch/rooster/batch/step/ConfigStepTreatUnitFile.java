package alo.spring.batch.rooster.batch.step;

import alo.spring.batch.rooster.control.ClassifierUnitItem;
import alo.spring.batch.rooster.database.RoosterFile;
import alo.spring.batch.rooster.database.UnitTransco;
import alo.spring.batch.rooster.model.unit.UnitItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

@Slf4j
@Configuration
public class ConfigStepTreatUnitFile {
    @Autowired
    StepBuilderFactory stepBuilderFactory;

    private RoosterFile roosterFile;

    private UnitTransco unitTransco;

    @Bean
    @StepScope
    public FlatFileItemReader<UnitItem> readerUnitItems(@Value("#{jobParameters['unitFile']}") Resource unitFile) {
        Assert.notNull(unitFile, "unitFile cannot be null, check jobs parameters!");

        return new FlatFileItemReaderBuilder<UnitItem>()
                .name("ReaderUnitItem")
                .resource(unitFile)
                .lineTokenizer(new DelimitedLineTokenizer(";"))
//                .fieldSetMapper(fieldSet -> new UnitItem(fieldSet, unitTransco))
                .lineMapper((line, lineNumber) -> new UnitItem(line, unitTransco))
                .build();

    }

    @Bean
    @StepScope
    public FlatFileItemWriter<UnitItem> writerUnitItems(@Value("#{jobParameters['outputFile']}") Resource outputFile) {
        return new FlatFileItemWriterBuilder<UnitItem>()
                .name("WriterUnitItem")
                .resource(outputFile)
                .lineAggregator(UnitItem::toString)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<UnitItem> goodWriterUnitItems(@Value("#{jobParameters['goodOutputFile']}") Resource outputFile) {
        return new FlatFileItemWriterBuilder<UnitItem>()
                .name("WriterUnitItem -> Good")
                .resource(outputFile)
                .lineAggregator(UnitItem::toString)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<UnitItem> badWriterUnitItems(@Value("#{jobParameters['badOutputFile']}") Resource outputFile) {
        return new FlatFileItemWriterBuilder<UnitItem>()
                .name("WriterUnitItem -> Bad")
                .resource(outputFile)
                .lineAggregator(UnitItem::toString)
                .build();
    }

    @Bean
    public ClassifierCompositeItemWriter<UnitItem> classifierItemWriter(
            @Qualifier("goodWriterUnitItems") ItemWriter<UnitItem> goodItemWriter,
            @Qualifier("badWriterUnitItems") ItemWriter<UnitItem> badItemWriter) {

        ClassifierUnitItem classifierUnitItem = new ClassifierUnitItem(goodItemWriter, badItemWriter);

        return new ClassifierCompositeItemWriterBuilder<UnitItem>()
                .classifier(classifierUnitItem)
                .build();
    }

    private StepExecutionListener listenerStep() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                JobExecution jobExecution = stepExecution.getJobExecution();
                ExecutionContext jobContext = jobExecution.getExecutionContext();
                roosterFile = (RoosterFile) jobContext.get(ConfigStepGetUnitFileInfo.UNIT_FILE_KEY);
                unitTransco = (UnitTransco) jobContext.get(ConfigStepGetUnitTransco.UNIT_TRANSCO_KEY);

                if (unitTransco != null) {
                    log.debug("unitTransco used : " + unitTransco.getFields());
                }
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                return null;
            }
        };
    }

    private ItemProcessor<UnitItem, UnitItem> processor() {
        return (item) -> {
            return item;
        };
    }

    @Bean
    public Step stepTreatUnitFile() {
        return stepBuilderFactory
                .get("stepTreatUnitFile")
                .listener(listenerStep())
                .<UnitItem, UnitItem>chunk(10)
                .reader(readerUnitItems(null))
                .processor(processor())
//                .writer(writerUnitItems(null))
                .writer(classifierItemWriter(null, null))
                .stream(goodWriterUnitItems(null))
                .stream(badWriterUnitItems(null))
                .listener(listenerStep())
                .build();
    }
}
