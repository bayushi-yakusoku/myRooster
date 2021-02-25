package alo.spring.batch.rooster.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.PathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import javax.sql.DataSource;
import java.sql.SQLException;

@Slf4j
@Configuration
public class ConfigDatabase {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource springDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "bank.datasource")
    public DataSource bankDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer1(@Qualifier("bankDataSource") DataSource datasource,
                                                        @Value("${bank.datasource.schema.init:false}") Boolean initSchema) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();

//        resourceDatabasePopulator.addScript(new ClassPathResource("schema_bank_data.sql"));
//        resourceDatabasePopulator.addScript(new ClassPathResource("insert_into_rooster_tables.sql"));
        resourceDatabasePopulator.addScript(new PathResource("src/main/resources/schema_bank_data.sql"));
        resourceDatabasePopulator.addScript(new PathResource("src/main/resources/insert_into_rooster_tables.sql"));

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(datasource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);

        dataSourceInitializer.setEnabled(initSchema);

        if (initSchema) {
            try {
                log.info("Schema initialisation : " + datasource.getConnection().getMetaData().getURL());
            } catch (SQLException e) {
                log.error("Error, cannot establish connection at this point...");

                return dataSourceInitializer;
            }
        }

        return dataSourceInitializer;
    }
}
