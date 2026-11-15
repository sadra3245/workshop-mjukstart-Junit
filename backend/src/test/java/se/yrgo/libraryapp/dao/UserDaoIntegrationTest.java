package se.yrgo.libraryapp.dao;
import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;


import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.radcortez.flyway.test.annotation.H2;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;
@Tag("integration")
@H2
public class UserDaoIntegrationTest {

    RoleDao roleDao;
    static DataSource ds;
    @BeforeAll
    static void initDataSource() {
// this way we do not need to create a new datasource every time
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        UserDaoIntegrationTest.ds = ds;

        Flyway flyway = Flyway.configure() .dataSource(ds).cleanDisabled(false).load();
        flyway.clean();
        flyway.migrate();
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

         roleDao = new RoleDao(ds);
       List<Role> roles = roleDao.get(loginInfo.get().getUserId());
        
       assertThat(roles).isNotEmpty();

    }



    @Test
    void getGenId(){

        UserDao userDao = new UserDao(ds);

        Optional<LoginInfo> loginInfo = userDao.getLoginInfo("test");

        assertThat(loginInfo).isNotEmpty();
    }
}

