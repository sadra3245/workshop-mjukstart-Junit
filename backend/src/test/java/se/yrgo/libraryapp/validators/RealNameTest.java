package se.yrgo.libraryapp.validators;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

public class RealNameTest {


    @ParameterizedTest
    @ValueSource(strings = {

            "balderdash","blimey","bullspit","damn","darn","drat","frack","frick","heck","shiet"
    }
    )
   public void badWordChecker(String name){

        assertThat(RealName.validate(name)).isFalse();

    }


    @ParameterizedTest
    @ValueSource(strings = {

            "balder","plimey","bullsswallow","damon","darnell","drey","france","france","hector","shyvanna"
    }
    )
    public void niceWordsChecker(String name){

        assertThat(RealName.validate(name)).isTrue();

    }



    /// balderdash
    /// blimey
    /// bullspit
    /// damn
    /// darn
    /// drat
    /// frack
    /// frick
    /// heck
    /// shiet

}
