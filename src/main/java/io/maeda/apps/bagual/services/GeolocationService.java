package io.maeda.apps.bagual.services;

import io.maeda.apps.bagual.clients.GeolocationClient;
import io.maeda.apps.bagual.dtos.Geolocation;
import io.maeda.apps.bagual.models.Redirect;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeolocationService {
    private final GeolocationClient geolocationClient;
    private final RedirectService redirectService;
    private final SimpMessagingTemplate template;

    @Async
    public Geolocation record(@Valid Redirect redirect, @NotNull String ip) {
        Geolocation geolocation = geolocationClient.resolve(ip);
        redirect.setCountry(geolocation.getCountryCode());
        redirect.setCity(geolocation.getCity());
        redirect.setCoordinates(geolocation);

        log.info("Recording redirection. " + geolocation);

        redirectService.save(redirect);

        template.convertAndSend("/topic/notification", geolocation);

        return geolocation;
    }
}
