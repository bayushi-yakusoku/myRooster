package alo.spring.batch.rooster.batch.step;

import alo.spring.batch.rooster.database.entity.RoosterFile;
import alo.spring.batch.rooster.database.UnitCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
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
public class ConfigStepGetUnitCheck {
    public static final String UNIT_CHECK_KEY = "unitCheck";

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    RoosterFile roosterFile;

    @Bean
    public Tasklet taskletGetUnitCheck(@Qualifier("bankDataSource") DataSource dataSource) {
        return (contribution, chunkContext) -> {
            UnitCheck unitCheck = new UnitCheck(roosterFile.getUnit(), dataSource);

            chunkContext.getStepContext().getStepExecution().getExecutionContext().put(UNIT_CHECK_KEY, unitCheck);

            return RepeatStatus.FINISHED;
        };
    }

    private StepExecutionListener stepExecutionListener() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
                roosterFile = (RoosterFile) executionContext.get(ConfigStepGetUnitFileInfo.UNIT_FILE_KEY);
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                return null;
            }
        };
    }

    private ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[]{ UNIT_CHECK_KEY });

        return listener;
    }

    private CompositeStepExecutionListener compositeStepExecutionListener() {
        CompositeStepExecutionListener listener = new CompositeStepExecutionListener();
        listener.register(stepExecutionListener());
        listener.register(promotionListener());

        return listener;
    }

    @Bean
    public Step stepGetUnitCheck() {
        return stepBuilderFactory
                .get("Step Get Unit Check")
                .tasklet(taskletGetUnitCheck(null))
                .listener(compositeStepExecutionListener())
                .build();
    }
}
