package alo.spring.batch.rooster.database.dao;

import alo.spring.batch.rooster.database.entity.unitcheck.Check;
import alo.spring.batch.rooster.database.entity.unitcheck.UnitCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;

@Slf4j
public class UnitCheckDaoImpl extends AbstractDaoImpl implements UnitCheckDao {

    public UnitCheckDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public UnitCheck getUnitCheck(String unit) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        UnitCheck unitCheck = new UnitCheck();

        jdbcTemplate.query("SELECT CHK.CHECK_NAME, CHK.SEVERITY, REF.CHECK_LABEL " +
                        "FROM BANK_DATA.UNIT_CHECK CHK, BANK_DATA.REF_CHECK REF " +
                        "WHERE CHK.CHECK_NAME = REF.CHECK_NAME AND " +
                        "CHK.UNIT_NAME = ?;",

                (resultSet) -> {
                    while (resultSet.next()) {
                        unitCheck.addCheck(new Check(resultSet.getString("CHECK_NAME"),
                                resultSet.getString("SEVERITY"),
                                resultSet.getString("CHECK_LABEL")));
                    }

                    return null;
                },

                unit);

        return unitCheck;
    }

    @Override
    public void saveUnitCheck(UnitCheck unitCheck) {

    }

    @Override
    public void updateUnitCheck(UnitCheck unitCheck) {

    }

    @Override
    public void deleteUnitCheck(UnitCheck unitCheck) {

    }
}
