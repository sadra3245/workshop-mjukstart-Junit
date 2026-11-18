package se.yrgo.libraryapp.dao;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.jooby.JoobyTest;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import se.yrgo.libraryapp.App;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;
@Testcontainers
@JoobyTest(value = App.class, factoryMethod = "createApp")
public class UserDaoIntegrationTest {

    @Container
    private final static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.29");
    private static DataSource ds;

    @BeforeAll
    static void setup() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(mysql.getJdbcUrl());
        hikariConfig.setUsername(mysql.getUsername());
        hikariConfig.setPassword(mysql.getPassword());
        hikariConfig.setDriverClassName(mysql.getDriverClassName());
        UserDaoIntegrationTest.ds = new HikariDataSource(hikariConfig);
        Flyway flyway = Flyway.configure().dataSource(ds).load();
        flyway.migrate();
    }

    public static App createApp() {
        System.setProperty("db.url", mysql.getJdbcUrl());
        System.setProperty("db.user", mysql.getUsername());
        System.setProperty("db.password", mysql.getPassword());
        return new App();
    }

    @AfterAll
    static void reset() {
        System.clearProperty("db.url");
        System.clearProperty("db.user");
        System.clearProperty("db.password");
    }


    @Test
    void getUserById() {
// this data comes from the test migration files
        final String username = "test";
        final UserId userId = UserId.of(1);
        UserDao userDao = new UserDao(ds);
        Optional<User> maybeUser = userDao.get(Integer.toString(userId.getId()));
        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.get().getName()).isEqualTo(username);
        assertThat(maybeUser.get().getId()).isEqualTo(userId);
    }

    @Test
    void addUserRole(){

    UserDao userDao = new UserDao(ds);

    boolean userCreated = userDao.register("sadra32","sadra","secret");

            assertThat(userCreated).isTrue();

            Optional<LoginInfo> loginInfo = userDao.getLoginInfo("sadra32");
        assertThat(loginInfo).isPresent();

        RoleDao roleDao = new RoleDao(ds);
       List<Role> roles = roleDao.get(loginInfo.get().getUserId());
        
       assertThat(roles).isNotEmpty();

    }



    @Test
    void getGenId(){
        UserDao userDao = new UserDao(ds);

        Optional<LoginInfo> loginInfo = userDao.getLoginInfo("test");

        assertThat(loginInfo).isNotEmpty();
    }

    @Test
    void sameUsersFailed(){

        UserDao userDao = new UserDao(ds);

        boolean user = userDao.register("sad","sadra","secret");

        boolean user1 = userDao.register("sad","sadra","secret");

        assertThat(user).isTrue();

        assertThat(user1).isFalse();
    }
}

