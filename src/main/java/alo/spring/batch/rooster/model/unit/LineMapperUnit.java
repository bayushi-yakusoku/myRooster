package alo.spring.batch.rooster.model.unit;

import alo.spring.batch.rooster.database.UnitTransco;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.LineMapper;

@Slf4j
public class LineMapperUnit implements LineMapper<UnitItem> {

    private final UnitTransco unitTransco;

    public LineMapperUnit(UnitTransco unitTransco) {
        this.unitTransco = unitTransco;
    }

    @Override
    public UnitItem mapLine(String line, int lineNumber) {
        return new UnitItem(line, unitTransco);
    }
}
