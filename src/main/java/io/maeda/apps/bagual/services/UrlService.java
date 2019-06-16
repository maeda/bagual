package io.maeda.apps.bagual.services;

import io.maeda.apps.bagual.models.Url;
import io.maeda.apps.bagual.repositories.UrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
@Slf4j
public class UrlService {
    private final UrlRepository urlRepository;

    public Url findOrSave(String url) {
        Url originalUrl = urlRepository.findFirstByOriginalUrl(url)
                .map(item -> item.copy().shortcutCount(item.getShortcutCount() + 1).build())
                .orElseGet(() -> Url.builder().originalUrl(url).build());

        return urlRepository.save(originalUrl);
    }

    public Optional<Url> find(String url) {
        return urlRepository.findFirstByOriginalUrl(url);
    }

    public Optional<Url> setUrlAsMalicious(String url) {
        log.info(url);
        Optional<Url> result = find(url);
        result.ifPresent(item -> item.setSuspect(true));

        return result;

    }
}
