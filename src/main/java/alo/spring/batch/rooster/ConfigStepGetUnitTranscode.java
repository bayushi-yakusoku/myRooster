package alo.spring.batch.rooster;

import alo.spring.batch.rooster.database.RoosterFile;
import alo.spring.batch.rooster.database.UnitTransco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class ConfigStepGetUnitTranscode {
    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    RoosterFile roosterFile;

    @Bean
    public Tasklet taskletGetUnitTranscode(@Qualifier("bankDataSource") DataSource dataSource) {
        return (contribution, chunkContext) -> {

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            UnitTransco unit = jdbcTemplate.query("select transco.field_name, transco.field_position \n" +
                            "from bank_data.unit_transco transco\n" +
                            "order by transco.field_name;",

                    (resultSet) -> {
                        UnitTransco unitTransco = new UnitTransco();

                        while (resultSet.next()) {
                            unitTransco.append(resultSet.getString("field_name"),
                                    resultSet.getInt("field_position"));
                        }

                        return unitTransco;
                    });

            log.debug("roosterFile is now : " + roosterFile.getFileName());
            log.debug("transco to save: " + unit);
            log.debug("Job instance : " + chunkContext.getStepContext().getJobInstanceId());

            ExecutionContext context = chunkContext.getStepContext().getStepExecution().getExecutionContext();
            context.put("transco", unit);

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
    public Step stepGetUnitTranscode() {
        return stepBuilderFactory
                .get("Step Get Unit Transcode")
                .tasklet(taskletGetUnitTranscode(null))
                .listener(promotionListener())
                .build();
    }
}
