package alo.spring.batch.rooster.model.unit;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class ControlStatus {
    enum Status {FAILED, PASSED, SKIPPED}
    enum Severity {ERROR, WARNING}

    private Severity severity;
    private String controlName;
    private String message;
    private Status status;

    public ControlStatus(Severity severity, String controlName, String message, Status status) {
        this.severity = severity;
        this.controlName = controlName;
        this.message = message;
        this.status = status;
    }

    public ControlStatus(Severity severity, String controlName, Status status) {
        this(severity, controlName, null, status);
    }

    public ControlStatus(String controlName, Status status) {
        this(Severity.WARNING, controlName, null, status);
    }

    public ControlStatus(String controlName) {
        this(Severity.WARNING, controlName, null, Status.PASSED);
    }

    @Override
    public String toString() {

        StringBuilder output = new StringBuilder();

        output.append("[" + severity + "/" + status + "] " + controlName);

        if (message != null)
            output.append(", " + message);

        return output.toString();

    }
}
