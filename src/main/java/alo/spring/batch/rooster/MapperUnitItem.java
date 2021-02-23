package alo.spring.batch.rooster;

import alo.spring.batch.rooster.database.UnitTransco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

@Slf4j
public class MapperUnitItem implements FieldSetMapper<UnitItem> {

    private final UnitTransco unitTransco;

    public MapperUnitItem(UnitTransco unitTransco) {
        this.unitTransco = unitTransco;
    }

    @Override
    public UnitItem mapFieldSet(FieldSet fieldSet) {
        return new UnitItem(fieldSet, unitTransco);
    }
}
