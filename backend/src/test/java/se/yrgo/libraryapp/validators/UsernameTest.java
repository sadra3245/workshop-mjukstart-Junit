package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class UsernameTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "sadra","Sadra@","sadra32"
    })
    void validateReturnsTrueIfBigLettersAndMoreThan4AndNumbs(String validNames) {
        assertThat(Username.validate(validNames)).isTrue();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "joe","sa dra"
})
@EmptySource
    void validateReturnsFalseIfLessThan4OrSpace(String invalidNames){
        assertThat(Username.validate(invalidNames)).isFalse();
    }




}
