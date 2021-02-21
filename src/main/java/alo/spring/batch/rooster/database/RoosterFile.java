package alo.spring.batch.rooster.database;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Data
@Component
public class RoosterFile {

    @Autowired
    @Qualifier("bankDataSource")
    private DataSource dataSource;

    private String fileName;
    private String signature;

    public RoosterFile() {
        this.fileName = "n/a";

        log.debug("############################################################## roosterFile : " + this.fileName);
    }

    public void updateDb() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(
                    "insert into bank_data.rooster_file (file_name, signature) " +
                            "values (?, ?);",
                    fileName,
                    signature
            );
    }
}
