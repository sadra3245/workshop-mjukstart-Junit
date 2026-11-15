package se.yrgo.libraryapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.pac4j.core.credentials.password.PasswordEncoder;
import se.yrgo.libraryapp.dao.UserDao;
import se.yrgo.libraryapp.entities.LoginInfo;
import se.yrgo.libraryapp.entities.User;
import se.yrgo.libraryapp.entities.UserId;
import se.yrgo.libraryapp.services.UserService;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UserServiceTest {
    @Mock
    private UserDao userDao;
    private UserService userService;
    @Mock
    private PasswordEncoder encoder ;


    @Test
    void correctLogin() {
        final String userId = "1";
        final UserId id = UserId.of(userId);
        final String username = "testuser";
        final String password = "password";
        final String passwordHash =
                "$argon2i$v=19$m=16,t=2,p=1$QldXU09Sc2dzOWdUalBKQw$LgKb6x4usOpDLTlXCBVhxA";
        final LoginInfo info = new LoginInfo(id, passwordHash);
        when(userDao.getLoginInfo(username)).thenReturn(Optional.of(info));
        when(encoder.matches(password,passwordHash)).thenReturn(true);
        UserService userService = new UserService(userDao,encoder);
        assertThat(userService.validate(username,
                password)).isEqualTo(Optional.of(id));
    }



    @ParameterizedTest
    @ValueSource(strings = {

     "sa","l"})
    @EmptySource
    void ifNameIsValidTest(String name){
        UserService userService1 = new UserService(userDao, encoder);

        assertThat(userService1.nameQualifierCheck(name)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "idkcoolpass=#!=","less4coo21k::@pass","bombo cla6more"

    })

    void passwordHashed(String password){
        UserService userService1 = new UserService(userDao,encoder);

        String hashedpass = userService1.hashPassword(password);


        assertThat(userService1.hashPassword(hashedpass)).isNotEqualTo(password);
       assertThat(encoder.matches(hashedpass,password)).isFalse();

        String hashedpass2 = userService1.hashPassword(password);

        assertThat(userService1.hashPassword(hashedpass2)).isNotEqualTo(password);
        assertThat(encoder.matches(hashedpass2,password)).isFalse();






    }
    /**
     * Encode a password.
     *
     * @param password the not encoded password
     * @return the encoded password
     */
//    String encode(String password);
//
//    /**
//     * Validate if a plainPassword matches
//     *
//     * @param plainPassword
//     *            the not encoded password to check
//     * @param encodedPassword
//     *            the encoded password
//     * @return <code>true</code> if they match
//     */
//    boolean matches(String plainPassword, String encodedPassword);

}