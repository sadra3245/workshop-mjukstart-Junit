package se.yrgo.libraryapp.dao;

import com.radcortez.flyway.test.annotation.H2;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import se.yrgo.libraryapp.entities.Role;
import se.yrgo.libraryapp.entities.UserId;

import javax.management.relation.RoleStatus;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("integration")
@H2
public class RoleDaoIntegrationTest {

    RoleDao roleDao;
    private static DataSource ds;

    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        RoleDaoIntegrationTest.ds = ds;

        Flyway flyway = Flyway.configure() .dataSource(ds).cleanDisabled(false).load();
        flyway.clean();
        flyway.migrate();
    }

    @BeforeEach
    void setup(){
        roleDao = new RoleDao(ds);

    }


    @Test
    void GetRoleByID(){

        UserId userId = UserId.of(1);
        List<Role> roles = roleDao.get(userId);

        assertThat(roles).isNotNull();

    }
    
}
