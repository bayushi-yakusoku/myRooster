package alo.spring.batch.rooster.control;

import alo.spring.batch.rooster.database.Check;
import alo.spring.batch.rooster.database.UnitCheck;
import alo.spring.batch.rooster.model.unit.UnitItem;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Checker {
    private final UnitCheck unitCheck;

    private final Map<String, checkRule> listChecks = new HashMap<>();

    public Checker(UnitCheck unitCheck) {
        this.unitCheck = unitCheck;

        initListChecks();

        checkListChecks();
    }

    private void checkListChecks() {
        for (Check check : unitCheck.getChecks()) {
            if (! listChecks.containsKey(check.getCheckName())) {
                log.warn("Check " + check.getCheckName() + " do NOT exist in JAVA code");
            }
        }
    }

    private void initListChecks() {
        log.info("Initialization of rules...");
        listChecks.put("CHECK_001", this::check001);
        listChecks.put("CHECK_002", this::check002);
    }

    public UnitItem performChecks(UnitItem item) {
        for (Check check : unitCheck.getChecks()) {
            if (listChecks.containsKey(check.getCheckName())) {
                listChecks.get(check.getCheckName()).action(item);
            }
        }

        return item;
    }

    private void check001(UnitItem item) {
        log.debug("Start Check001 on " + item.getLine());
    }

    private void check002(UnitItem item) {
        log.debug("Start Check002 on " + item.getLine());
    }

    private void checkUtf8(UnitItem item) {
        log.debug("Start checkUtf8 on " + item.getLine());
    }
}

@FunctionalInterface
interface checkRule {
    void action(UnitItem item);
}
