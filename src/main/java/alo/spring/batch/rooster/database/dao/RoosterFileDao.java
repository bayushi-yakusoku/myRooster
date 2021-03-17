package alo.spring.batch.rooster.database.dao;

import alo.spring.batch.rooster.database.entity.roosterfile.RoosterFile;

public interface RoosterFileDao {

    RoosterFile getRoosterFile(String fileName);

    void saveRoosterFile(RoosterFile roosterFile);
    void updateRoosterFile(RoosterFile roosterFile);
    void deleteRoosterFile(String fileName);

}
