package se.yrgo.libraryapp.controller;

import io.jooby.Context;
import io.jooby.Cookie;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.yrgo.libraryapp.controllers.LogoutController;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.entities.UserId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import javax.sql.DataSource;
import java.util.UUID;

public class LogoutControllerTest {

    private LogoutController logoutController;
    private static SessionDao sessionDao;
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1");
        LogoutControllerTest.ds = ds;

        Flyway flyway = Flyway.configure().dataSource(ds).cleanDisabled(false).load();
        flyway.clean();
        flyway.migrate();
    }

    @BeforeEach
    void setup(){

        sessionDao = new SessionDao(ds);
        logoutController = new LogoutController();
    }


    @Test
    void logedout(){

        UserId userId = UserId.of(1);
        UUID session = sessionDao.create(userId);

        assertThat(sessionDao.validate(session)).isEqualTo(userId);


        Cookie cookie = new Cookie("session","");
    }
}
