package io.maeda.apps.bagual;

import lombok.SneakyThrows;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.net.InetAddress;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = BagualTestConfiguration.class)
@ActiveProfiles(value = "it")
@Transactional
@AutoConfigureTestDatabase
public abstract class AbstractIntegrationTest {
    @Autowired
    protected MockMvc mvc;

    protected ResultActions call(String serverName, MockHttpServletRequestBuilder action) throws Exception {
        return mvc.perform(action.with(
                (request) -> configureRequest(request, serverName)));
    }

    @SneakyThrows
    protected String getCurrentHostAddress() {
        return InetAddress.getLocalHost().getHostAddress();
    }

    private MockHttpServletRequest configureRequest(MockHttpServletRequest request, String serverName) {
        request.setServerName(serverName);
        request.setRemoteAddr(getCurrentHostAddress());
        return request;
    }
}
