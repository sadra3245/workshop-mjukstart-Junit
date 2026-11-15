package se.yrgo.libraryapp.dao;

import com.radcortez.flyway.test.annotation.H2;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.pac4j.core.exception.CredentialsException;
import se.yrgo.libraryapp.entities.UserId;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Tag("integration")
@H2
public class SessionDaoIntegrationTest{

    private SessionDao sessionDao;
    private static DataSource ds;
    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        SessionDaoIntegrationTest.ds = ds;

        Flyway flyway = Flyway.configure() .dataSource(ds).cleanDisabled(false).load();
        flyway.clean();
        flyway.migrate();
    }

    @BeforeEach
    void setup(){
        sessionDao = new SessionDao(ds);

    }


    @Test
    void created(){
        UserId userId = UserId.of(1);

        UUID uuidList = sessionDao.create(userId);

        assertThat(uuidList).isNotNull();

    }


    @Test
    void deleted(){

        UserId userId = UserId.of(1);

        UUID uuidList = sessionDao.create(userId);
        assertThat(uuidList).isNotNull();
        sessionDao.delete(uuidList);

       assertThatThrownBy(() -> {
           sessionDao.validate(uuidList);
       }).isInstanceOf(CredentialsException.class);


    }

    @Test
    void validated(){
        UserId userId = UserId.of(1);

        UUID uuidList = sessionDao.create(userId);

       UserId userId1 = sessionDao.validate(uuidList);

            assertThat(userId1).isNotNull();
            assertThat(userId1).isEqualTo(userId);
    }



}
