package se.yrgo.libraryapp.controller;

import com.radcortez.flyway.test.annotation.H2;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pac4j.core.credentials.password.PasswordEncoder;
import se.yrgo.libraryapp.controllers.RegisterUserController;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.forms.RegisterUserData;
import se.yrgo.libraryapp.services.UserService;

import javax.sql.DataSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RegisterUserControllerTest {

    private static DataSource ds;
    private RegisterUserController registerUserController;
    private UserDao userDao;
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:register_test;MODE=MySQL;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        ds = dataSource;
        Flyway flyway = Flyway.configure().dataSource(ds).cleanDisabled(false).load();
        flyway.clean();
        flyway.migrate();
    }

    @BeforeEach()
    void setup(){

        userDao = new UserDao(ds);
        UserService userService = new UserService(userDao,passwordEncoder);
        registerUserController = new RegisterUserController(userService);

    }


    @Test
    void register(){
        RegisterUserData registerUserData = new RegisterUserData("sadra32","sadra","secret");

        assertThat(registerUserController.register(registerUserData)).isTrue();

        assertThat(userDao.isNameAvailable("sadra32")).isFalse();


    }
}
