package alo.spring.batch.rooster.database;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class UnitTransco {
    private Map<String, Integer> fields = new HashMap<String, Integer>();

    public void append(String field, Integer pos) {
        fields.put(field, pos);
        log.info("Ajout de " + field + " en position " + pos);
    }

    public Integer getPos(String fieldName) {
        return (Integer) fields.get(fieldName);
    }
}
