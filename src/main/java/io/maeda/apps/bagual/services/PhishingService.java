package io.maeda.apps.bagual.services;

import io.maeda.apps.bagual.models.Alias;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.repositories.PhishingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhishingService {
    private final ShortUrlService shortUrlService;
    private final AliasService aliasService;
    private final PhishingRepository phishingRepository;

    public Optional<ShortUrl> isPhishing(String aliasName, String shortcut) {
        Alias alias = aliasService.find(aliasName);
        Optional<ShortUrl> shortUrl = shortUrlService.find(alias, shortcut);

        return shortUrl.filter(item -> item.getUrl().isSuspect());
    }

    @PostConstruct
    public void process() {
        phishingRepository.process();
    }
}
