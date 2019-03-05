package io.maeda.apps.bagual.services;

import io.maeda.apps.bagual.dtos.RedirectRequest;
import io.maeda.apps.bagual.models.Redirect;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.repositories.RedirectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedirectService {
    private final RedirectRepository redirectRepository;
    private final ShortUrlService shortUrlService;
    private final Clock clock;

    public Optional<ShortUrl> record(@Valid RedirectRequest request) {
        Optional<ShortUrl> url = shortUrlService.find(request.getAlias(), request.getShortcut());

        return url.map(item -> record(item, request));
    }

    private ShortUrl record(@Valid ShortUrl url, @Valid RedirectRequest request) {

        Redirect redirect = Redirect.builder()
                .shortUrl(url)
                .httpReferer(Optional.ofNullable(request.getReferer()).orElse(""))
                .httpUserAgent(request.getUserAgent())
                .redirectStatus(String.valueOf(HttpStatus.OK.value()))
                .requestTime(Redirect.getRequestTimeCalculated(LocalDateTime.now(clock)))
                .remoteAddr(request.getRemoteAddr())
                .build();

        log.info(String.format("Recording redirection. Url: short=%s, original%s", url.getShortUrl(), url.getUrl().getOriginalUrl()));

        redirectRepository.save(redirect);

        return url;
    }

    public int clicks(@NotNull ShortUrl url) {
        return redirectRepository.findAllByShortUrl(url).size();
    }
}
