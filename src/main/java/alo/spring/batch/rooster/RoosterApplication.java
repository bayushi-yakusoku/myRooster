package alo.spring.batch.rooster;

import alo.spring.batch.rooster.batch.job.ParameterAddRunTime;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
//@Import(ConfigStepGetUnitTranscode.class)
public class RoosterApplication {

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Bean
	public Job jobGetUnitTransco(@Qualifier("stepGetUnitFileInfo") 	Step stepGetUnitFileInfo,
								 @Qualifier("stepGetUnitTransco") Step stepGetUnitTransco,
								 @Qualifier("stepTreatUnitFile") 	Step stepTreatUnitFile) {
		return jobBuilderFactory
				.get("Rooster")
				.incrementer(new ParameterAddRunTime())
				.start(stepGetUnitFileInfo)
				.next(stepGetUnitTransco)
				.next(stepTreatUnitFile)
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(RoosterApplication.class, args);
	}

}
