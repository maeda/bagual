package io.maeda.apps.bagual;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ApiControllerTest extends AbstractIntegrationTest {

    @Test
    public void shouldIncreaseUrlCountForShortUrlsAlreadyThere() throws Exception {
        call("bagu.al", get("/api.txt?q=http://google.com.br"))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("http://bagu.al/6V"));
    }

    @Test
    public void shouldShortNewUrl() throws Exception{
        call("bagu.al", get("/api.txt?q=http://example.com")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("http://bagu.al/1Fg"));
    }

    @Test
    public void shouldDoNotGenerateShortUrlKeyTwiceForSameAliasAndUrl() throws Exception{
        call("bagu.al", get("/api.txt?q=http://example.com")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("http://bagu.al/1Fg"));

        call("bagu.al", get("/api.txt?q=http://example.com")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("http://bagu.al/1Fg"));
    }
}