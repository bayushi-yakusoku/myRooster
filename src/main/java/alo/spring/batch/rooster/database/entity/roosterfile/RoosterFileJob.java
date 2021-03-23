package alo.spring.batch.rooster.database.entity.roosterfile;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;

@Slf4j
@Data
public class RoosterFileJob {
    private Integer jobInstanceId;
    private Date creationDate;
}
