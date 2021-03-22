package alo.spring.batch.rooster.database.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.PathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

//@ExtendWith(SpringExtension.class)
//@JdbcTest
//@Sql({"file:F:/Code/InteliJ/Java/rooster/src/test/resources/sql/schema_test.sql",
//        "file:F:/Code/InteliJ/Java/rooster/src/test/resources/sql/insert_into_schema_test.sql"})
//@ContextConfiguration(classes = ConfigDatabase.class)
//@SpringBootTest(args = {"unitFile=file:F:/Code/Items/Rooster/NATION_roosterFile.csv",
//        "outputFile=file:F:/Code/Items/Rooster/Output_RoosterFile.csv",
//        "goodOutputFile=file:F:/Code/Items/Rooster/Good_RoosterFile.csv",
//        "badOutputFile=file:F:/Code/Items/Rooster/Bad_RoosterFile.csv"})
@SpringBootTest()
class RoosterFileDaoImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("bankDataSource")
    DataSource dataSource;

    @BeforeEach
    void setUp() {
//        Resource resource = new ClassPathResource("CreateDefaultDB.sql");
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();

        databasePopulator.addScript(new PathResource("src/test/resources/sql/schema_bank_data-test.sql"));
        databasePopulator.addScript(new PathResource("src/test/resources/sql/insert_into_rooster_tables-test.sql"));
        databasePopulator.execute(dataSource);
    }

//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void getRoosterFile() {
//    }

    @Test
    void whenInjectInMemoryDataSource_thenReturnCorrectEmployeeCount() {
        System.out.println("pouf!");
    }
}
