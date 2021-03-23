package alo.spring.batch.rooster.batch.step;

import alo.spring.batch.rooster.database.dao.UnitTranscoDaoImpl;
import alo.spring.batch.rooster.database.entity.roosterfile.RoosterFile;
import alo.spring.batch.rooster.database.entity.transco.UnitTransco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.CompositeStepExecutionListener;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Slf4j
@Configuration
public class ConfigStepGetUnitTransco {
    public static final String UNIT_TRANSCO_KEY = "transco";

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private RoosterFile roosterFile;

    @Bean
    public Tasklet taskletGetUnitTransco(@Qualifier("bankDataSource") DataSource dataSource) {
        return (contribution, chunkContext) -> {
            UnitTranscoDaoImpl transcoDao = new UnitTranscoDaoImpl(dataSource);

            UnitTransco unitTransco = transcoDao.getTransco(roosterFile.getUnit());

            ExecutionContext context = chunkContext.getStepContext().getStepExecution().getExecutionContext();
            context.put(ConfigStepGetUnitFileInfo.UNIT_FILE_KEY, roosterFile);
            context.put(UNIT_TRANSCO_KEY, unitTransco);

            return RepeatStatus.FINISHED;
        };
    }

    private StepExecutionListener listenerStep() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.debug("");

                JobExecution jobExecution = stepExecution.getJobExecution();
                ExecutionContext jobContext = jobExecution.getExecutionContext();

                roosterFile = (RoosterFile) jobContext.get(ConfigStepGetUnitFileInfo.UNIT_FILE_KEY);
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                return null;
            }
        };
    }

    public ExecutionContextPromotionListener promotionListener() {
        log.info("Creating the Promoting things...");

        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

        listener.setKeys(new String[] {ConfigStepGetUnitFileInfo.UNIT_FILE_KEY, UNIT_TRANSCO_KEY});

        return listener;
    }

    public CompositeStepExecutionListener compositeStepExecutionListener() {
        CompositeStepExecutionListener listener = new CompositeStepExecutionListener();

        listener.register(listenerStep());
        listener.register(promotionListener());

        return listener;
    }

    @Bean
    public Step stepGetUnitTransco() {
        return stepBuilderFactory
                .get("Step Get Unit Transco")
                .tasklet(taskletGetUnitTransco(null))
                .listener(compositeStepExecutionListener())
                .build();
    }
}
