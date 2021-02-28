package alo.spring.batch.rooster.control;

import alo.spring.batch.rooster.model.unit.UnitItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

@Slf4j
public class ClassifierUnitItem implements Classifier<UnitItem, ItemWriter<? super UnitItem>> {

    private final ItemWriter<UnitItem> goodItemWriter;
    private final ItemWriter<UnitItem> badItemWriter;

    @Override
    public ItemWriter<UnitItem> classify(UnitItem unitItem) {

        if (unitItem.isGood()) {
            return goodItemWriter;
        }
        else {
            return badItemWriter;
        }
    }

    public ClassifierUnitItem(ItemWriter<UnitItem> goodItemWriter, ItemWriter<UnitItem> badItemWriter) {
        this.goodItemWriter = goodItemWriter;
        this.badItemWriter = badItemWriter;
    }
}
