package alo.spring.batch.rooster.database.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class UnitTransco {

    @Setter(AccessLevel.NONE)
    private Map<String, FieldInfo> fields = new HashMap<>();

    @Setter(AccessLevel.NONE)
    private String unit;

    public void append(String field, FieldInfo fieldInfo) {
        fields.put(field, fieldInfo);
    }

    public Integer getPos(String fieldName) {
        return fields.get(fieldName).getPosition();
    }

    public Boolean isMandatory(String fieldName) {
        return fields.get(fieldName).getIsMandatory();
    }

}

