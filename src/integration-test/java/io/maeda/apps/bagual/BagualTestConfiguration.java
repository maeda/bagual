package io.maeda.apps.bagual;

import lombok.SneakyThrows;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

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

}
