package alo.spring.batch.rooster;

import alo.spring.batch.rooster.database.UnitTransco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UnitTransco unitTransco;

    @Bean
    @StepScope
    public FlatFileItemReader<UnitItem> readerUnitItems(@Value("#{jobParameters['unitFile']}") Resource unitFile) {
        Assert.notNull(unitFile, "unitFile cannot be null, check jobs parameters!");

        return new FlatFileItemReaderBuilder<UnitItem>()
                .name("ReaderUnitItem")
                .resource(unitFile)
                .lineTokenizer(new DelimitedLineTokenizer(";"))
//                .fieldSetMapper(new MapperUnitItem(unitTransco))
                .lineMapper(new LineMapperUnit(unitTransco))
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

    private StepExecutionListener listenerStep() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.debug("unitTransco used : " + unitTransco.getFields());

//                JobExecution jobExecution = stepExecution.getJobExecution();
//                ExecutionContext jobContext = jobExecution.getExecutionContext();
//                unitTransco = (UnitTransco) jobContext.get("transco");
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
                .writer(writerUnitItems(null))
                .build();
    }
}
