package Model;

import java.util.Arrays;
import java.util.Collection;

public class TestDataProvider {

    public static Collection<Object[]> getBrowserTypes() {
        return Arrays.asList(new Object[][]{
                {"chrome"},
                {"yandex"}
        });
    }
}
