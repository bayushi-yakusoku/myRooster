package alo.spring.batch.rooster.database.entity.unitcheck;

import lombok.Data;

@Data
public class Check {
    private String checkName;
    private String severity;
    private String checkLabel;

    public Check(String checkName, String severity, String checkLabel) {
        this.checkName = checkName;
        this.severity = severity;
        this.checkLabel = checkLabel;
    }
}
