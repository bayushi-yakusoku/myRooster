package alo.spring.batch.rooster;

import alo.spring.batch.rooster.database.RoosterFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Configuration
public class ConfigStepGetUnitFileInfos {
    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    RoosterFile roosterFile;

    @Bean
    @StepScope
    public Tasklet taskletGetUnitFileInfo(@Value("#{jobParameters['unitFile']}") Resource unitFile) {
        return (contribution, chunkContext) -> {
            String fileName = unitFile.getFilename();
            String signature = checksum(unitFile.getFile().getPath());

            log.debug("file name : " + fileName);
            log.debug("file signature : " + signature);

            roosterFile.setFileName(fileName);
            roosterFile.setSignature(signature);
            roosterFile.updateDb();

            return RepeatStatus.FINISHED;
        };

    }

    @Bean
    public Step stepGetUnitFileInfo() {
        return stepBuilderFactory
                .get("stepGetUnitFileInfo")
                .tasklet(taskletGetUnitFileInfo(null))
                .build();
    }

    private String checksum(String filepath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // file hashing with DigestInputStream
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md)) {
            while (dis.read() != -1) ; //empty loop to clear the data
            md = dis.getMessageDigest();
        }

        // bytes to hex
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }
}
