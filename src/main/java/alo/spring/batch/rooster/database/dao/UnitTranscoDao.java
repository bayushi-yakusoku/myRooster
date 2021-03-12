package alo.spring.batch.rooster.database.dao;

import alo.spring.batch.rooster.database.entity.transco.UnitTransco;

public interface UnitTranscoDao {
    UnitTransco getTransco(String unit);

    void updateTransco(UnitTransco transco);
    void saveTransco(UnitTransco transco);
    void deleteTransco(UnitTransco transco);
}
