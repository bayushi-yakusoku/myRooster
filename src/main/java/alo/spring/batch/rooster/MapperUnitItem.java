package alo.spring.batch.rooster;

import alo.spring.batch.rooster.database.UnitTransco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

@Slf4j
public class MapperUnitItem implements FieldSetMapper<UnitItem> {

    private UnitTransco unitTransco;

    public MapperUnitItem(UnitTransco unitTransco) {
        this.unitTransco = unitTransco;
    }

    @Override
    public UnitItem mapFieldSet(FieldSet fieldSet) throws BindException {
        UnitItem item = new UnitItem();

//        log.info("Mapping to UnitItem");
//        log.info("Transco : " + unitTransco.getFields());

        item.setId(fieldSet.readInt(unitTransco.getPos("ID") - 1));

        item.setFirstname(fieldSet.readString(unitTransco.getPos("FIRSTNAME") - 1));
        item.setLastname(fieldSet.readString(unitTransco.getPos("LASTNAME") - 1));

        item.setAge(fieldSet.readInt(unitTransco.getPos("AGE") - 1));

        item.setAddress(fieldSet.readString(unitTransco.getPos("ADDRESS") - 1));
        item.setCountry(fieldSet.readString(unitTransco.getPos("COUNTRY") - 1));

        return item;
    }
}
