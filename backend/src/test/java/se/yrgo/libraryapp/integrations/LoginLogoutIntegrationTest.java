package se.yrgo.libraryapp.integrations;

import io.jooby.Context;
import io.jooby.Cookie;
import org.assertj.core.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.pac4j.core.exception.CredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.yrgo.libraryapp.controllers.LoginController;
import se.yrgo.libraryapp.dao.RoleDao;
import se.yrgo.libraryapp.dao.SessionDao;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.entities.forms.LoginData;
import se.yrgo.libraryapp.services.UserService;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.mock;

@Tag("integration")
    @Testcontainers
    public class LoginLogoutIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(LoginLogoutIntegrationTest.class);
    private static DataSource ds;
    private  UserService userService;
    private   RoleDao roleDao;
    private   UserDao userDao;
    private  SessionDao sessionDao;
private  LoginData loginData;
    private  Context context;

    private LoginController loginController;



        @Container
        private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>
                ("mysql:8.0.29");
        @Test
        public void alwaysOkToLogout() throws IOException {
        }

    @BeforeEach void setup(){
            loginData = new LoginData("sadra", "secret");
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
    public void loginOutSuccess(){

            userService.registerUser("sadra32","sadra","secret");
        Optional<UserId> optionalUserId = userService.validate("sadra32","secret");

       UserId userId = optionalUserId.get();

            UUID session = sessionDao.create(userId);
            sessionDao.validate(session);
            Cookie cookie = loginController.createSessionCookie(session);
            List<Role> roleList = loginController.login(context,session.toString(),loginData);
            List<Role> roles = loginController.isLoggedIn(session.toString());

             assertThat(roles).isNotNull();

             sessionDao.delete(session);

             assertThatThrownBy(() -> sessionDao.validate(session)).isInstanceOf(CredentialsException.class);

        }

        @Test
        void wrongUserLogin(){

            UserId userId = UserId.of(4);
            userService.registerUser("idk","nobody","no");
            userService.validate("idk","secret");

            UUID session =  sessionDao.create(userId);
            sessionDao.validate(session);

            Cookie cookie = loginController.createSessionCookie(session);
            List<Role> roleList = loginController.login(context,cookie.toString(),loginData);

            List<Role> role = loginController.isLoggedIn(session.toString());

        }

    }
