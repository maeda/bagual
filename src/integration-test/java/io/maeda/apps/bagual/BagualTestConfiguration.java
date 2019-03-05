package io.maeda.apps.bagual;

import io.maeda.apps.bagual.helpers.PhishingDataReader;
import lombok.SneakyThrows;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@TestConfiguration
@Import(ApplicationConfig.class)
public class BagualTestConfiguration {

    @Bean
    @SneakyThrows
    public Clock clock() {
        return Clock.fixed(Instant.parse("2018-06-01T10:15:30.00Z"), ZoneId.systemDefault());
    }

    @Bean
    public PhishingDataReader phishingDataReader() throws Exception {
        PhishingDataReader mock = Mockito.mock(PhishingDataReader.class);

        Mockito.doReturn(new FileReader(new ClassPathResource("online-valid.csv").getFile()))
                .when(mock).getPhishingDataReader();

        return mock;
    }

    @SpyBean
    RestTemplate restTemplate;
}
