package alo.spring.batch.rooster.model.unit;

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
    private String line = null;
    private Boolean isGood = true;

    private String address;
    private String age;
    private String country;
    private String firstname;
    private String id;
    private String lastname;

    @Setter(AccessLevel.NONE)
    List<ControlStatus> controls = new ArrayList<>();

    public void addControlResult(ControlStatus controlStatus) {

        if(controlStatus.getSeverity() == ControlStatus.Severity.ERROR &&
                controlStatus.getStatus() == ControlStatus.Status.FAILED) {
            isGood = false;
        }

        this.controls.add(controlStatus);
    }

    public UnitItem(FieldSet fieldSet, UnitTransco transco) {
        address = getValue("ADDRESS", fieldSet, transco);
        age = getValue("AGE", fieldSet, transco);
        country = getValue("COUNTRY", fieldSet, transco);
        firstname = getValue("FIRSTNAME", fieldSet, transco);
        id = getValue("ID", fieldSet, transco);
        lastname = getValue("LASTNAME", fieldSet, transco);
    }

    public UnitItem(String line, UnitTransco transco) {
        this.line = line;

        String[] fields = line.split(";", -1);

        address = getValue("ADDRESS", fields, transco);
        age = getValue("AGE", fields, transco);
        country = getValue("COUNTRY", fields, transco);
        firstname = getValue("FIRSTNAME", fields, transco);
        id = getValue("ID", fields, transco);
        lastname = getValue("LASTNAME", fields, transco);
    }

    @Nullable
    private String getValue(String fieldName, String[] fields, UnitTransco transco) {
        String value;

        try {
            value = fields[transco.getPos(fieldName) - 1];
        }
        catch (IndexOutOfBoundsException e) {
            addControlResult(new ControlStatus(ControlStatus.Severity.ERROR,
                    "Field " + fieldName + " Index (" + transco.getPos(fieldName) + ") Out Of Bounds",
                    ControlStatus.Status.FAILED));
            return null;
        }

        if (value.isEmpty() && transco.isMandatory(fieldName) ) {
            addControlResult(new ControlStatus("Field " + fieldName + " is mandatory", ControlStatus.Status.FAILED));
        }

        return value;
    }

    @Nullable
    private String getValue(String fieldName, FieldSet fieldSet, UnitTransco transco) {
        String value;

        try {
            value = fieldSet.readString(transco.getPos(fieldName) - 1);
        } catch (IndexOutOfBoundsException e) {
            addControlResult(new ControlStatus(ControlStatus.Severity.ERROR,
                    "Field " + fieldName + " Index (" + transco.getPos(fieldName) + ") Out Of Bounds",
                    ControlStatus.Status.FAILED));
            return null;
        }

        if (value.isEmpty() && transco.isMandatory(fieldName) ) {
            addControlResult(new ControlStatus("Field " + fieldName + " is mandatory", ControlStatus.Status.FAILED));
        }

        return value;
    }

    public Boolean isGood() {
        return isGood;
    }

    @Override
    public String toString() {

        StringBuilder output = new StringBuilder();

        output.append("UnitItem{" +
                "address='" + address + '\'' +
                ", age='" + age + '\'' +
                ", country='" + country + '\'' +
                ", firstname='" + firstname + '\'' +
                ", id='" + id + '\'' +
                ", lastname='" + lastname + '\'' +
                "}\n");

        if ( line != null) {
            output.append("From line: " + line + "\n" );
        }

        for (ControlStatus control:
                controls) {
            output.append(" - " + control + "\n");
        }

        return output.toString();
    }
}
