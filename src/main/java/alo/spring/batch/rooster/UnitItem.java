package alo.spring.batch.rooster;

import alo.spring.batch.rooster.database.UnitTransco;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class UnitItem {
    private String address;
    private String age;
    private String country;
    private String firstname;
    private String id;
    private String lastname;

    @Setter(AccessLevel.NONE)
    List<ControlStatus> controls = new ArrayList<>();

    public void addControlResult(ControlStatus controlStatus) {
        this.controls.add(controlStatus);
    }

    public UnitItem() {};

    public UnitItem(FieldSet fieldSet, UnitTransco transco) {
        address = getValue("ADDRESS", fieldSet, transco);
        age = getValue("AGE", fieldSet, transco);
        country = getValue("COUNTRY", fieldSet, transco);
        firstname = getValue("FIRSTNAME", fieldSet, transco);
        id = getValue("ID", fieldSet, transco);
        lastname = getValue("LASTNAME", fieldSet, transco);
    }

    @Nullable
    private String getValue(String fieldName, FieldSet fieldSet, UnitTransco transco) {
        String value = null;
        try {
            value = fieldSet.readString(transco.getPos(fieldName) - 1);
        } catch (IndexOutOfBoundsException e) {
            addControlResult(new ControlStatus("Field " + fieldName + " does not exist", ControlStatus.Status.FAILED));
            return null;
        }

        if (value.isEmpty() && transco.isMandatory(fieldName) ) {
            addControlResult(new ControlStatus("Field " + fieldName + " is mandatory", ControlStatus.Status.FAILED));
        }

        return value;
    }
}
