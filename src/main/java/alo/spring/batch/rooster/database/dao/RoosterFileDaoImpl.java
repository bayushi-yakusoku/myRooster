package alo.spring.batch.rooster.database.dao;

import alo.spring.batch.rooster.database.entity.RoosterFile;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
public class RoosterFileDaoImpl extends AbstractDaoImpl implements RoosterFileDao{
    @Override
    public RoosterFile getRoosterFile(String fileName) {
        return null;
    }

    @Override
    public void saveRoosterFile(@NonNull RoosterFile roosterFile) throws DataAccessException {
        Assert.notNull(roosterFile.getFileName(), "filename cannot be null pomalo!");
        Assert.notNull(roosterFile.getSignature(), "signature cannot be null pomalo!");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        jdbcTemplate.update(
                "INSERT INTO BANK_DATA.ROOSTER_FILE (FILE_NAME, SIGNATURE, UNIT_NAME) " +
                        "VALUES (?, ?, ?);",
                roosterFile.getFileName(),
                roosterFile.getSignature(),
                roosterFile.getUnit()
        );
    }

    @Override
    public void updateRoosterFile(RoosterFile roosterFile) {
    }

    @Override
    public void deleteRoosterFile(String fileName) {
    }
}
