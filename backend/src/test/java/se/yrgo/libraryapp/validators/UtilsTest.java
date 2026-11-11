package se.yrgo.libraryapp.validators;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.Console;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class UtilsTest {
    @ParameterizedTest
    @CsvSource({
            "1234, ''",
            "sadrafghjkllpoiutyeqwerzxcvbnm, sadrafghjkllpoiutyeqwerzxcvbnm",
            "sad ra, sad ra",
            "sadra231, sadra",
            "DASDAK, dasdak",
            "Sadra!, sadra",
            "Sa-Dra?, sadra",
            "4sshole,sshole",
            "2o2al,oal"
    })
    void returnsAStringWithLettersAndWhiteSpace(String invalidName, String validName ){


        assertThat(Utils.onlyLettersAndWhitespace(invalidName)).isEqualTo(validName);

    }

    @Test
    void throwExceptionIfNull(){

        assertThatExceptionOfType(Throwable.class).isThrownBy(() -> Utils.onlyLettersAndWhitespace(null));
        }

    @ParameterizedTest
    @CsvSource( {
            "1l3e4a5s6b7t8b0o, lleessbbttbboo",
    "sadra?#_, sadra"})

    void ok(String name, String expected){

        assertThat(Utils.cleanAndUnLeet(name)).isEqualTo(expected);
    }


    @Test
    void testCleanAndUnLeetWithEmptyString() {
        String result = Utils.cleanAndUnLeet("");
        assertThat(result).isEqualTo("");
    }
}
