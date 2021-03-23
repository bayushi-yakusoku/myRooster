package alo.spring.batch.rooster.database.dao;

import alo.spring.batch.rooster.database.entity.roosterfile.RoosterFile;
import lombok.NonNull;
import org.springframework.lang.Nullable;

public interface RoosterFileDao {

    @Nullable
    RoosterFile getRoosterFile(@NonNull String fileName, @NonNull String signature);

    void saveRoosterFile(@NonNull RoosterFile roosterFile);
    void updateRoosterFile(@NonNull RoosterFile roosterFile);
    void deleteRoosterFile(@NonNull String fileName, @NonNull String signature);

}
