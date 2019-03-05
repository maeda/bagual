package io.maeda.apps.bagual.controllers;

import io.maeda.apps.bagual.AbstractIntegrationTest;
import io.maeda.apps.bagual.models.Redirect;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.repositories.RedirectRepository;
import io.maeda.apps.bagual.services.AliasService;
import io.maeda.apps.bagual.services.ShortUrlService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiControllerTest extends AbstractIntegrationTest {

    @Autowired
    private RedirectRepository redirectRepository;

    @Autowired
    private ShortUrlService shortUrlService;

    @Autowired
    private AliasService aliasService;

    @Value("${io.maeda.bagual.alias:bagu.al}")
    private String defaultAlias;

    @Test
    public void shouldRedirectToOriginalUrl() throws Exception {
        call(defaultAlias, get("/6V"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://google.com.br"));

        ShortUrl shortUrl = shortUrlService.find(aliasService.find(defaultAlias), "6V").orElseThrow(IllegalStateException::new);

        Collection<Redirect> redirects = redirectRepository.findAllByShortUrl(shortUrl);

        assertThat(redirects.isEmpty(), equalTo(Boolean.FALSE));

        Redirect redirect = redirects.iterator().next();
        assertThat(redirect, equalTo(buildRedirect(shortUrl)));

    }

    @Test
    public void shouldShowUrlDetails() throws Exception {
        call(defaultAlias, get("/6V+"))
                .andExpect(status().isOk());

    }

    @Test
    public void shouldBlockRedirectForMaliciousUrl() throws Exception {
        call(defaultAlias, get("/1uf"))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/warning?shortUrl=http://%s/1uf&url=http://204787org.ukit.me/", defaultAlias)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldShowMaliciousUrlPage() throws Exception {
        call(defaultAlias, get(String.format("/warning?shortUrl=http://%s/1uf&url=http://204787org.ukit.me/", aliasService)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    private Redirect buildRedirect(ShortUrl shortUrl) {
        return Redirect.builder()
                .requestTime(1527848130L)
                .remoteAddr("127.0.0.1")
                .redirectStatus(String.valueOf(HttpStatus.OK.value()))
                .shortUrl(shortUrl)
                .id(1L)
                .build();
    }

    @Test
    public void shouldShorteningAndUrl() throws Exception {
        call("bagu.al", post("/api/shortening")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"url\":\"http://example.com\"}")
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"code\":\"200\",\"message\":\"CREATED\",\"content\":\"http://bagu.al/1Fg\"}"));

    }
}
