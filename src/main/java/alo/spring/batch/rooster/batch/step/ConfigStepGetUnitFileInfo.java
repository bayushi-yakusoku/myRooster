package alo.spring.batch.rooster.batch.step;

import alo.spring.batch.rooster.database.RoosterFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;

import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Configuration
public class ConfigStepGetUnitFileInfo {
    public static final String UNIT_FILE_KEY = "unitFile";

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public Tasklet taskletGetUnitFileInfo(@Value("#{jobParameters['unitFile']}") Resource unitFile,
                                          @Qualifier("bankDataSource") DataSource dataSource) {
        return (contribution, chunkContext) -> {
            String fileName = unitFile.getFilename();
            String signature = checksum(unitFile.getFile().getPath());

            RoosterFile roosterFile = new RoosterFile(fileName, signature);

            log.debug("file name : " + fileName);
            log.debug("file signature : " + signature);

            roosterFile.setFileName(fileName);
            roosterFile.setSignature(signature);

            try {
                roosterFile.updateDb(dataSource);
            } catch (DataAccessException e) {
                log.error("Error during db update for file/signature : " + fileName + "/" + signature);
                log.error(e.getMessage());
                if (log.isDebugEnabled()) {
                    e.printStackTrace();
                }

                throw e;
            }

            ExecutionContext context = chunkContext.getStepContext().getStepExecution().getExecutionContext();
            context.put(UNIT_FILE_KEY, roosterFile);

            return RepeatStatus.FINISHED;
        };
    }

    private ExecutionContextPromotionListener promotionListener() {
        log.info("Creating the Promoting things...");

        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

        listener.setKeys(new String[] {UNIT_FILE_KEY});

        return listener;
    }

    @Bean
    public Step stepGetUnitFileInfo() {
        return stepBuilderFactory
                .get("stepGetUnitFileInfo")
                .tasklet(taskletGetUnitFileInfo(null, null))
                .listener(promotionListener())
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
