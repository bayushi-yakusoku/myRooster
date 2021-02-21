package alo.spring.batch.rooster;

import alo.spring.batch.rooster.database.RoosterFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import java.io.BufferedInputStream;
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

        byte[] digest;

        // file hashing with DigestInputStream
        try (DigestInputStream dis = new DigestInputStream(
                new BufferedInputStream(new FileInputStream(filepath)),
                md)
        ) {
            //noinspection StatementWithEmptyBody
            while (dis.read() != -1) ; // Empty loop to process the data

            digest = md.digest();
        }

        return Hex.encodeHexString(digest, false);
    }
}
