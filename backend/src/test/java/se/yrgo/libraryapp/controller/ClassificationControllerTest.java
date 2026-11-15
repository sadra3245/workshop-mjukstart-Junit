package se.yrgo.libraryapp.controller;

import io.jooby.Context;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.yrgo.libraryapp.controllers.ClassificationController;
import se.yrgo.libraryapp.controllers.LoginController;
import se.yrgo.libraryapp.controllers.LogoutController;
import se.yrgo.libraryapp.dao.ClassificationDao;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.forms.LoginData;
import se.yrgo.libraryapp.services.UserService;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

public class ClassificationControllerTest {

   private ClassificationController classificationController;
   private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1");
        ClassificationControllerTest.ds = ds;

        Flyway flyway = Flyway.configure().dataSource(ds).cleanDisabled(false).load();
        flyway.clean();
        flyway.migrate();
    }


    @BeforeEach
    void setup(){

        ClassificationDao classificationDao = new ClassificationDao(ds);
        classificationController = new ClassificationController(classificationDao);
    }

    @Test
    void get(){

       assertThat( classificationController.getAvailable()).isNotNull();

    }
}
