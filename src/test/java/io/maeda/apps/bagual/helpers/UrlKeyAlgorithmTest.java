package io.maeda.apps.bagual.helpers;

import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

public class UrlKeyAlgorithmTest {

    @Test
    public void shouldGenerateUrlKey() {
        UrlKeyAlgorithm algorithm = new UrlKeyAlgorithm();

        assertThat(algorithm.generate(27523), equalTo("79V"));
    }
}