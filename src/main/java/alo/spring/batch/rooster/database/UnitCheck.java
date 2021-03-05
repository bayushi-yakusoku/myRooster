package alo.spring.batch.rooster.database;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class UnitCheck {
    @Setter(AccessLevel.NONE)
    private String unitName;

    @Setter(AccessLevel.NONE)
    private List<Check> checks = new ArrayList<>();

    public UnitCheck(String unitName, DataSource dataSource) {
        this.unitName = unitName;

        getUnitCheck(dataSource);
    }

    private void getUnitCheck(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        jdbcTemplate.query("SELECT CHK.CHECK_NAME, CHK.SEVERITY, REF.CHECK_LABEL " +
                        "FROM BANK_DATA.UNIT_CHECK CHK, BANK_DATA.REF_CHECK REF " +
                        "WHERE CHK.CHECK_NAME = REF.CHECK_NAME AND " +
                        "CHK.UNIT_NAME = ?;",

                (resultSet) -> {
                    while (resultSet.next()) {
                        checks.add(new Check(resultSet.getString("CHECK_NAME"),
                                resultSet.getString("SEVERITY"),
                                resultSet.getString("CHECK_LABEL")));
                    }

                    return null;
                },

                unitName);

    }
}
