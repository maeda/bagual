package io.maeda.apps.bagual.helpers;


import io.maeda.apps.bagual.dtos.ShortUrlVO;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class ShortUrlHelperTest {

    private ShortUrlHelper component = new ShortUrlHelper();

    @Test
    public void shouldDecomposeAShortenedUrl() {
        testDecomposition("https://bagu.al/A", "A");
        testDecomposition("http://bagu.al/A", "A");
        testDecomposition("bagu.al/A", "A");
        testDecomposition("bagu.al/ABCD", "ABCD");
    }

    @Test
    public void shouldCheckIfItIsAShortenedUrl() {
        assertThat(component.isAShortenedUrl("https://bagu.al/A"), is(true));
        assertThat(component.isAShortenedUrl("bagu.al/A"), is(true));
        assertThat(component.isAShortenedUrl("bagu.al"), is(false));
        assertThat(component.isAShortenedUrl("https://bagu.al"), is(false));
        assertThat(component.isAShortenedUrl("https://bagu.al/"), is(false));
        assertThat(component.isAShortenedUrl("https://bagu.al//"), is(false));
        assertThat(component.isAShortenedUrl("//bagu.al//"), is(false));
        assertThat(component.isAShortenedUrl("http://google.com"), is(false));
    }

    private void testDecomposition(String shortUrlAsString, String shortcut) {
        Optional<ShortUrlVO> shortUrl = component.decompose(shortUrlAsString);

        assertThat(shortUrl.isPresent(), is(true));
        assertThat(shortUrl.get().getAlias(), is("bagu.al"));
        assertThat(shortUrl.get().getShortcut(), is(shortcut));

    }
}