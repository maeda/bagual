package io.maeda.apps.bagual;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminApiControllerTest extends AbstractIntegrationTest {

    @Value("${io.maeda.apps.bagual.auth_token}")
    private String authToken;

    @Test
    public void shouldSetAnUrlAsMalicious() throws Exception {
        call("bagu.al", delete("/api/phishing/bagu.al/6V").header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("http://google.com.br"));

        call("bagu.al", get("/6V"))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/warning?shortUrl=http://bagu.al/6V&url=http://google.com.br"));
    }

    @Test
    public void shouldIgnoreRequestWithInvalidAuthorization() throws Exception {
        call("bagu.al", delete("/api/phishing/bagu.al/6V").header("Authorization", "wrong"))
                .andExpect(status().isUnauthorized());
    }
}
