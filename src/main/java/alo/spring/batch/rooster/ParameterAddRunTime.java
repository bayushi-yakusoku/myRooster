package alo.spring.batch.rooster;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.lang.Nullable;

import java.util.Date;

/**
 * Add current time as a job parameter
 */
@Slf4j
public class ParameterAddRunTime implements JobParametersIncrementer {

    private static final String runTimeKey = "run.time";

    /**
     * Update job's parameters with one called "run.time" and set its value
     * with current time
     *
     * @param parameters the job's original parameter
     * @return updated parameters
     */
    @Override
    public JobParameters getNext(@Nullable JobParameters parameters) {
        JobParameters nextParameters = (parameters == null) ? new JobParameters() : parameters;

        log.info("Adding date to job parameters list");

        return new JobParametersBuilder(nextParameters).addDate(runTimeKey, new Date()).toJobParameters();
    }
}
