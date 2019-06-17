package io.maeda.apps.bagual;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminApiControllerTest extends AbstractIntegrationTest {

    @Value("${io.maeda.apps.bagual.auth_token}")
    private String authToken;

    @Test
    public void shouldSetAnUrlAsMalicious() throws Exception {
        call("bagu.al", put("/api/phishing/bagu.al/1ue").header("Authorization", authToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("http://mainiawa.weebly.com"));

        call("bagu.al", get("/1ue"))
                .andExpect(status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/warning?shortUrl=http://bagu.al/1ue&url=http://mainiawa.weebly.com"));
    }

    @Test
    public void shouldIgnoreRequestWithInvalidAuthorization() throws Exception {
        call("bagu.al", put("/api/phishing/bagu.al/1uf").header("Authorization", "wrong"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldMarkUrlAsDeletedInsteadRemoveIt() throws Exception {
        call("bagu.al", get("/1ue"))
                .andExpect(status().isFound());
        call("bagu.al", delete("/api/bagu.al/1ue").header("Authorization", authToken))
                .andExpect(status().isOk());
        call("bagu.al", get("/1ue"))
                .andExpect(status().isNotFound());
    }
}
