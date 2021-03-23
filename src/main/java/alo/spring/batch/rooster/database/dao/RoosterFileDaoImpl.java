package alo.spring.batch.rooster.database.dao;

import alo.spring.batch.rooster.database.entity.roosterfile.RoosterFile;
import alo.spring.batch.rooster.database.entity.roosterfile.RoosterFileInfo;
import alo.spring.batch.rooster.database.entity.roosterfile.RoosterFileJob;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import javax.sql.DataSource;
import java.util.List;

@Slf4j
public class RoosterFileDaoImpl extends AbstractDaoImpl implements RoosterFileDao{
    public RoosterFileDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    @Nullable
    public RoosterFile getRoosterFile(@NonNull String fileName, @NonNull String signature) throws DataAccessException {
        RoosterFile roosterFile = getCoreValues(fileName, signature);

        if (roosterFile == null)
            return null;

        roosterFile.setListJob(getJobs(roosterFile.getRoosterFileId()));

        roosterFile.setListInfo(getFileInfo(roosterFile.getRoosterFileId()));

        return roosterFile;
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
    public void updateRoosterFile(@NonNull RoosterFile roosterFile) {
        Assert.notNull(roosterFile.getFileName(), "filename cannot be null pomalo!");
        Assert.notNull(roosterFile.getSignature(), "signature cannot be null pomalo!");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        RoosterFile existingRoosterFile = getRoosterFile(roosterFile.getFileName(), roosterFile.getSignature());

        if (existingRoosterFile != null) {
            update(roosterFile);
        }
        else {
            insert(roosterFile);
        }
    }

    private RoosterFile getCoreValues(String fileName, String signature) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        List<RoosterFile> roosterFiles = jdbcTemplate.query(
                "SELECT ROOSTER_FILE_ID, FILE_NAME, SIGNATURE, UNIT_NAME " +
                        "FROM BANK_DATA.ROOSTER_FILE " +
                        "WHERE FILE_NAME = ? AND SIGNATURE = ?;",
                (resultSet, i) -> {
                    RoosterFile file = new RoosterFile();

                    file.setRoosterFileId(resultSet.getInt(1));
                    file.setFileName(resultSet.getString(2));
                    file.setSignature(resultSet.getString(3));
                    file.setUnit(resultSet.getString(4));

                    return file;
                },
                fileName,
                signature
        );

        if (roosterFiles.isEmpty())
            return null;
        else
            return roosterFiles.get(0);
    }

    private List<RoosterFileJob> getJobs(Integer fileId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        return  jdbcTemplate.query(
                "SELECT JOB_INSTANCE_ID, CREATION_DATE " +
                        "FROM BANK_DATA.ROOSTER_FILE_JOB " +
                        "WHERE ROOSTER_FILE_ID = ?;",
                (resultSet, i) -> {
                    RoosterFileJob fileJob = new RoosterFileJob();

                    fileJob.setJobInstanceId(resultSet.getInt(1));
                    fileJob.setCreationDate(resultSet.getDate(2));

                    return fileJob;
                },
                fileId
        );
    }

    private List<RoosterFileInfo> getFileInfo (Integer fileId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        return jdbcTemplate.query(
                "SELECT ROOSTER_FILE_ID, INFO_NAME, INFO_VALUE " +
                        "FROM BANK_DATA.ROOSTER_FILE_INFO " +
                        "WHERE ROOSTER_FILE_ID = ?;",
                (resultSet, i) -> {
                    RoosterFileInfo fileInfo = new RoosterFileInfo();

                    fileInfo.setInfoName(resultSet.getString(1));
                    fileInfo.setInfoValue(resultSet.getString(2));

                    return fileInfo;
                },
                fileId
        );
    }

    private void insert(RoosterFile roosterFile) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        jdbcTemplate.update(
                "insert into bank_data.rooster_file (file_name, signature, unit_name) values (?, ?, ?);",
                roosterFile.getFileName(),
                roosterFile.getSignature(),
                roosterFile.getUnit()
        );

    }

    private void update(RoosterFile roosterFile) {

    }

    @Override
    public void deleteRoosterFile(String fileName, String signature) {
        Assert.notNull(fileName, "filename cannot be null pomalo!");
        Assert.notNull(signature, "signature cannot be null pomalo!");

    }
}
