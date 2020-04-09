package components.functions.ef;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinearErrorFunctionTest {

    @Test
    void calculateErrorValue() {
        LinearErrorFunction ef = new LinearErrorFunction(299, 1196);
        double errorValue = ef.calculateErrorValue(1794, 2093);

        double correctValue = 2.0/3.0;

        assertEquals(correctValue, errorValue);
    }
}