package alo.spring.batch.rooster.database;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Component
public class UnitTransco {
    @Autowired
    @Qualifier("bankDataSource")
    @Setter(AccessLevel.NONE)
    private DataSource dataSource;

    @Setter(AccessLevel.NONE)
    private Map<String, FieldInfo> fields = new HashMap<>();

    private String unit = "null";

    private void append(String field, FieldInfo fieldInfo) {
        fields.put(field, fieldInfo);
    }

    public Integer getPos(String fieldName) {
        return fields.get(fieldName).getPosition();
    }

    public Boolean isMandatory(String fieldName) {
        return fields.get(fieldName).getIsMandatory();
    };

    public void setUnit(String unit) {
        this.unit = unit;
        getTransco();
    }

    private void getTransco() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        UnitTransco unit = jdbcTemplate.query(
                "select transco.field_name, transco.field_position, transco.field_is_mandatory " +
                        "from bank_data.unit_transco transco " +
                        "where transco.unit_name = ? " +
                        "order by transco.field_name;",

                (resultSet) -> {
                    while (resultSet.next()) {
                        FieldInfo fieldInfo = new FieldInfo();
                        fieldInfo.setPosition(resultSet.getInt("field_position"));
                        fieldInfo.setIsMandatory(resultSet.getBoolean("field_is_mandatory"));

                        this.append(resultSet.getString("field_name"), fieldInfo);
                    }

                    return null;
                },
                this.unit);

        log.debug("transco found : " + fields);
    }
}

@Data
class FieldInfo {
    private Integer position;
    private Boolean isMandatory;
}
