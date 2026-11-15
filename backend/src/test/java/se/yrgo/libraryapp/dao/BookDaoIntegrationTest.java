package se.yrgo.libraryapp.dao;

import com.radcortez.flyway.test.annotation.H2;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import se.yrgo.libraryapp.entities.Book;
import se.yrgo.libraryapp.entities.BookEdition;
import se.yrgo.libraryapp.entities.BookId;
import se.yrgo.libraryapp.entities.DdsClassification;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("integration")
@H2
public class BookDaoIntegrationTest {

private BookDao bookDao;
    private static DataSource ds;
    @BeforeAll
    static void initDataSource() {
        final JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:test");
        BookDaoIntegrationTest.ds = ds;

        Flyway flyway = Flyway.configure() .dataSource(ds).cleanDisabled(false).load();
        flyway.clean();
        flyway.migrate();
    }

    @BeforeEach
    void setup(){
        bookDao = new BookDao(ds);

    }

    @Test
    void getBookById(){

        BookId bookId = BookId.of(1);

        Optional<Book> optional = bookDao.get(bookId);

        assertThat(optional.isPresent()).isTrue();

        Book book = optional.get();
        assertThat(book.getId()).isEqualTo(bookId);
        assertThat(book).isNotNull();

    }




    @Test
    void findBookById(){

        BookId bookId = BookId.of(1);

        Optional<Book> book = bookDao.get(bookId);

        assertThat(book).isPresent();

        assertThat(book.get().getId()).isEqualTo(bookId);

    }


    @Test
    void findBookByTitle(){

        Set<BookEdition> bookEditions = bookDao.find(null, "T-SQL Fundamentals", null);


        assertThat(bookEditions).isNotNull();


    }

    @Test
    void findBookByIsbn(){

        Set<BookEdition> bookEditions = bookDao.find("9781509302000" ,null, null);


        assertThat(bookEditions).isNotNull();


    }

    @Test
    void findByClassification(){
        DdsClassification classification = new DdsClassification(20,"Book rarities");

        Set<BookEdition> bookEditions = bookDao.withClass(classification);

        assertThat(bookEditions).isNotNull();
    }


}
