package alo.spring.batch.rooster.database.dao;

import alo.spring.batch.rooster.database.entity.unitcheck.UnitCheck;

public interface UnitCheckDao {
    UnitCheck getUnitCheck(String unit);

    void saveUnitCheck(UnitCheck unitCheck);
    void updateUnitCheck(UnitCheck unitCheck);
    void deleteUnitCheck(UnitCheck unitCheck);
}
