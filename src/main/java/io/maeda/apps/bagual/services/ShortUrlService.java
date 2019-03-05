package io.maeda.apps.bagual.services;

import io.maeda.apps.bagual.helpers.UrlKeyAlgorithm;
import io.maeda.apps.bagual.models.Alias;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.models.Url;
import io.maeda.apps.bagual.repositories.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final UrlKeyAlgorithm algorithm;

    public ShortUrl findOrSave(@NotNull Alias alias, Url url) {

        return shortUrlRepository.findByCompanyAndAliasNameAndUrl(alias.getCompany(), alias.getAliasName(), url)
                .orElseGet(() -> this.save(alias, url));
    }

    public Optional<ShortUrl> find(@NotNull Alias alias, String shortcut) {

        return shortUrlRepository.findByCompanyAndAliasNameAndShortcut(alias.getCompany(), alias.getAliasName(), shortcut);
    }

    private ShortUrl save(Alias aliasEntity, Url url) {

        aliasEntity.getSeed().increment();

        ShortUrl shortUrl = ShortUrl.builder()
                .aliasName(aliasEntity.getAliasName())
                .alias(aliasEntity)
                .company(aliasEntity.getCompany())
                .shortcut(algorithm.generate(aliasEntity.getSeed().getSeed()))
                .url(url)
                .build();

        return shortUrlRepository.save(shortUrl);
    }
}
