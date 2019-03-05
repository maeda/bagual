package io.maeda.apps.bagual.clients;

import io.maeda.apps.bagual.dtos.Geolocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeolocationClient {

    private final RestTemplate restTemplate;

    @Value("${io.maeda.apps.bagual.geolocation-service-url:http://ip-api.com/json/}")
    private String geolocationServiceUrl;

    public Geolocation resolve(String ip) {
        ResponseEntity<Geolocation> entity = restTemplate.getForEntity(geolocationServiceUrl + ip, Geolocation.class);
        if(!HttpStatus.OK.equals(entity.getStatusCode())) {
            log.warn("Failed to get location information. Ip: " + ip);
            return Geolocation.builder().build();
        }
        return entity.getBody();
    }
}
