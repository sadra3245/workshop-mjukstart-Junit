package se.yrgo.libraryapp.controller;

import io.jooby.Context;
import io.jooby.Cookie;
import io.jooby.SameSite;
import io.jooby.StatusCode;
import io.jooby.annotations.CookieParam;
import io.jooby.annotations.GET;
import io.jooby.annotations.POST;
import io.jooby.annotations.Path;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pac4j.core.exception.CredentialsException;
import se.yrgo.libraryapp.controllers.LoginController;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.forms.LoginData;
import se.yrgo.libraryapp.services.UserService;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {


    private static DataSource ds;
    private  UserService userService;
    private   RoleDao roleDao;
    UserDao userDao;
    private  SessionDao sessionDao;

    private  Context context;
    LoginController loginController;

    private static LoginData loginData;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_DELAY=-1");
        LoginControllerTest.ds = ds;

        Flyway flyway = Flyway.configure().dataSource(ds).cleanDisabled(false).load();
        flyway.clean();
        flyway.migrate();
    }


    @BeforeEach void setup(){
        loginData = new LoginData("sadra","secret");
        userDao = new UserDao(ds);
        sessionDao = new SessionDao(ds);
        roleDao = new RoleDao(ds);
        context = mock(Context.class);

        org.pac4j.core.credentials.password.PasswordEncoder encoder =
                new org.pac4j.core.credentials.password.SpringSecurityPasswordEncoder(
                        new org.springframework.security.crypto.argon2.Argon2PasswordEncoder()
                );

        userService = new UserService(userDao,encoder);
        loginController = new LoginController(userService,roleDao,sessionDao);
    }


    @Test
    void doesItGetIt(){

        userService.registerUser("sadra32","sadra","secret");
        UserId userId = new UserId(1);
        userService.validate("sadra","secret");

        UUID session = sessionDao.create(userId);
       assertThat(sessionDao.create(userId)).isNotNull();
        assertThat(sessionDao.validate(session)).isNotNull();

    }



    @Test
    void sessionCookieCreated(){

        UUID session = UUID.randomUUID();

        Cookie cookie = loginController.createSessionCookie(session);
        assertThat( cookie.setSameSite(SameSite.LAX)).isNotNull();
        assertThat( cookie.setHttpOnly(true)).isNotNull();
        assertThat(cookie.setMaxAge(-1)).isEqualTo(cookie);

    }


    @Test
    void  loginSuccess() {

        userService.registerUser("sadra32", "sadra", "secret");

        Optional<UserId> user = userService.validate("sadra32", "secret");

        assertThat(user).isNotEmpty();
        UserId userId = user.get();

        UUID session = sessionDao.create(userId);

        assertThat(sessionDao.validate(session)).isNotNull();
        assertThat(sessionDao.validate(session)).isEqualTo(userId);
    }

    @Test
    void loggedInActually(){

        userService.registerUser("sadra32","sadra","secret");
        UserId userId = userService.validate("sadra32","secret").get();
       UUID session = sessionDao.create(userId);

        List<Role> roles = loginController.isLoggedIn(session.toString());

        assertThat(roles).isNotNull();

        }




    }


