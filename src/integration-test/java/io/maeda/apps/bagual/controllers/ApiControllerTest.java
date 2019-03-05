package io.maeda.apps.bagual.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.maeda.apps.bagual.AbstractIntegrationTest;
import io.maeda.apps.bagual.dtos.Geolocation;
import io.maeda.apps.bagual.models.Redirect;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.repositories.RedirectRepository;
import io.maeda.apps.bagual.services.AliasService;
import io.maeda.apps.bagual.services.ShortUrlService;
import io.maeda.apps.bagual.services.UrlService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ApiControllerTest extends AbstractIntegrationTest {

    @Autowired
    private UrlService urlService;

    @Autowired
    private RedirectRepository redirectRepository;

    @Autowired
    private ShortUrlService shortUrlService;

    @Autowired
    private AliasService aliasService;

    @Value("${io.maeda.bagual.alias:bagu.al}")
    private String defaultAlias;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldRedirectToOriginalUrl() throws Exception {
        Mockito.doReturn(ResponseEntity.ok(mapper.readValue(new ClassPathResource("geolocation_test.json").getFile(), Geolocation.class)))
                .when(restTemplate).getForEntity(Mockito.anyString(), Mockito.eq(Geolocation.class));

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

    @Test
    public void shouldMarkUrlAsMalicious() throws Exception {
        urlService.setUrlAsMalicious("http://mainiawa.weebly.com");
        call(defaultAlias, get("/api/config/security/phishing/load"))
                .andExpect(status().isOk());

        assertThat(urlService.find("http://mainiawa.weebly.com").get().isSuspect(), equalTo(Boolean.TRUE));
    }

    @Test
    public void shouldIncrementSeed() throws Exception {
        call("bagu.al", post("/api/shortening")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"url\":\"http://example.com\"}")
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"code\":\"200\",\"message\":\"CREATED\",\"content\":\"http://bagu.al/1Fh\"}"));

        call("bagu.al", post("/api/shortening")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"url\":\"http://bla.com\"}")
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"code\":\"200\",\"message\":\"CREATED\",\"content\":\"http://bagu.al/1Fi\"}"));

    }

    @Test
    public void shouldBlockShorteningAnAlreadyShortenedUrl() throws Exception {
        call("bagu.al", post("/api/shortening")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"url\":\"http://bagu.al/6V\"}")
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"code\":\"200\",\"message\":\"CREATED\",\"content\":\"http://bagu.al/6V\"}"));
    }

    @SneakyThrows
    private Redirect buildRedirect(ShortUrl shortUrl) {
        return Redirect.builder()
                .requestTime(1527848130L)
                .remoteAddr(this.getCurrentHostAddress())
                .redirectStatus(String.valueOf(HttpStatus.OK.value()))
                .shortUrl(shortUrl)
                .country("BR")
                .city("Viamão")
                .coordinates(Geolocation.builder()
                        .city("Viamão")
                        .countryCode("BR")
                        .latitude(-30.0833D)
                        .longitude(-51.0333D).build())
                .id(1L)
                .build();
    }

    @Test
    public void shouldShorteningAnUrl() throws Exception {
        call("bagu.al", post("/api/shortening")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"url\":\"http://example.com\"}")
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"code\":\"200\",\"message\":\"CREATED\",\"content\":\"http://bagu.al/1Fh\"}"));

    }
}
