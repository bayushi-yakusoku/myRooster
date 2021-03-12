package alo.spring.batch.rooster.database.entity.unitcheck;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class UnitCheck {
    @Setter(AccessLevel.NONE)
    private String unitName;

    @Setter(AccessLevel.NONE)
    private List<Check> checks = new ArrayList<>();

    public void addCheck(Check check) {
        checks.add(check);
    }
}
