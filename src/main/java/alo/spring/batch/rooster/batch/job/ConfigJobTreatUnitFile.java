package alo.spring.batch.rooster.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
//@Import(ConfigStepGetUnitTransco.class)
public class ConfigJobTreatUnitFile {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job jobTreatUnitFile(@Qualifier("stepGetUnitFileInfo")   Step stepGetUnitFileInfo,
                                @Qualifier("stepGetUnitTransco")    Step stepGetUnitTransco,
                                @Qualifier("stepGetUnitCheck")  	Step stepGetUnitCheck,
                                @Qualifier("stepTreatUnitFile") 	Step stepTreatUnitFile) {
        return jobBuilderFactory
                .get("Rooster")
                .incrementer(new ParameterAddRunTime())
                .start(stepGetUnitFileInfo)
                .next(stepGetUnitTransco)
                .next(stepGetUnitCheck)
                .next(stepTreatUnitFile)
                .build();
    }
}
