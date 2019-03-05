package io.maeda.apps.bagual.services;

import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.models.Url;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShortenerService {
    private final UrlService urlService;
    private final ShortUrlService shortUrlService;
    private final AliasService aliasService;


    public ShortUrl shorten(String alias, String url) {
        Url originalUrl = urlService.findOrSave(url);

        return shortUrlService.findOrSave(aliasService.find(alias), originalUrl);
    }

}
