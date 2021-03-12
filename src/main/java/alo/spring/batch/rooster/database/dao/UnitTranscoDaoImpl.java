package alo.spring.batch.rooster.database.dao;

import alo.spring.batch.rooster.database.entity.transco.FieldInfo;
import alo.spring.batch.rooster.database.entity.transco.UnitTransco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UnitTranscoDaoImpl extends AbstractDaoImpl implements UnitTranscoDao {

    @Override
    public UnitTransco getTransco(String unit) throws DataAccessException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        UnitTransco unitTransco = new UnitTransco();

        jdbcTemplate.query(
                "SELECT TRANSCO.FIELD_NAME, TRANSCO.FIELD_POSITION, TRANSCO.FIELD_IS_MANDATORY " +
                        "FROM BANK_DATA.UNIT_TRANSCO TRANSCO " +
                        "WHERE TRANSCO.UNIT_NAME = ? " +
                        "ORDER BY TRANSCO.FIELD_NAME;",

                (resultSet) -> {
                    while (resultSet.next()) {
                        FieldInfo fieldInfo = new FieldInfo();
                        fieldInfo.setPosition(resultSet.getInt("field_position"));
                        fieldInfo.setIsMandatory(resultSet.getBoolean("field_is_mandatory"));

                        unitTransco.append(resultSet.getString("field_name"), fieldInfo);
                    }

                    return null;
                },
                unit);

        log.debug("transco found : " + unitTransco.getFields());

        return unitTransco;
    }

    @Override
    public void updateTransco(UnitTransco transco) {

    }

    @Override
    public void saveTransco(UnitTransco transco) {

    }

    @Override
    public void deleteTransco(UnitTransco transco) {

    }
}
