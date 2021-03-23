package alo.spring.batch.rooster.database.dao;

import javax.sql.DataSource;

public abstract class AbstractDaoImpl {
    private final DataSource dataSource;

    protected DataSource getDataSource() {
        return dataSource;
    }

    public AbstractDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
