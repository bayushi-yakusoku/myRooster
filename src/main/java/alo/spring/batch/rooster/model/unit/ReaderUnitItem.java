package alo.spring.batch.rooster.model.unit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.*;

@Slf4j
public class ReaderUnitItem implements ItemStreamReader<UnitItem> {
    @Override
    public UnitItem read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {

    }
}
