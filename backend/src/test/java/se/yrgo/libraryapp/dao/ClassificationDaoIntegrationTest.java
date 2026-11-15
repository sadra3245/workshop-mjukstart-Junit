package se.yrgo.libraryapp.dao;

import com.radcortez.flyway.test.annotation.H2;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import se.yrgo.libraryapp.entities.DdsClassification;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("integration")
@H2
public class ClassificationDaoIntegrationTest {

    ClassificationDao classificationDao;
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        ClassificationDaoIntegrationTest.ds = ds;

        Flyway flyway = Flyway.configure() .dataSource(ds).cleanDisabled(false).load();
        flyway.clean();
        flyway.migrate();
    }

    @BeforeEach
    void setup(){
        classificationDao = new ClassificationDao(ds);

    }

    void getByClassification() {
    List<DdsClassification> list = classificationDao.getAvailable();

    assertThat(list).isNotNull();

    }
}
