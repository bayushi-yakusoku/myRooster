package alo.spring.batch.rooster.database.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;

public abstract class AbstractDaoImpl {
    @Autowired
    @Qualifier("bankDataSource")
    private DataSource dataSource;

    protected DataSource getDataSource() {
        return dataSource;
    }
}
