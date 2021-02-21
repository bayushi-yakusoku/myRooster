package alo.spring.batch.rooster;

import alo.spring.batch.rooster.database.RoosterFile;
import alo.spring.batch.rooster.database.UnitTransco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Slf4j
@Configuration
public class ConfigStepGetUnitTranscode {
    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    RoosterFile roosterFile;

    @Autowired
    UnitTransco unitTransco;

    @Bean
    public Tasklet taskletGetUnitTransco(@Qualifier("bankDataSource") DataSource dataSource) {
        return (contribution, chunkContext) -> {
            unitTransco.setUnit(roosterFile.getUnit());

//            ExecutionContext context = chunkContext.getStepContext().getStepExecution().getExecutionContext();
//            context.put("transco", unitTransco);

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        log.info("Creating the Promoting things...");

        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

        listener.setKeys(new String[] {"transco"});

        return listener;
    }

    @Bean
    public Step stepGetUnitTransco() {
        return stepBuilderFactory
                .get("Step Get Unit Transco")
                .tasklet(taskletGetUnitTransco(null))
                .listener(promotionListener())
                .build();
    }
}
