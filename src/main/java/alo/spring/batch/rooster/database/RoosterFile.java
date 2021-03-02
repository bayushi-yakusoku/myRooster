package alo.spring.batch.rooster.database;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
public class RoosterFile {

    private String fileName;
    private String signature;

    @Setter(AccessLevel.NONE)
    private String unit;

    public RoosterFile(String fileName, String signature) {
        this.fileName = fileName;
        this.signature = signature;

        getUnitFromFileName();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
        this.unit = getUnitFromFileName();
    }

    public void updateDb(DataSource dataSource) throws DataAccessException {
        Assert.notNull(fileName, "filename cannot be null pomalo!");
        Assert.notNull(signature, "signature cannot be null pomalo!");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.update(
                    "INSERT INTO BANK_DATA.ROOSTER_FILE (FILE_NAME, SIGNATURE) " +
                            "VALUES (?, ?);",
                    fileName,
                    signature
            );
    }

    @Nullable
    private String getUnitFromFileName() {
        Pattern pattern = Pattern.compile("^(\\p{Alpha}*)_");
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.find()) {
            return matcher.group(1);
        }
        else
            return "n/a";
    }
}
