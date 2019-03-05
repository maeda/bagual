package io.maeda.apps.bagual.services;

import io.maeda.apps.bagual.helpers.ShortUrlHelper;
import io.maeda.apps.bagual.models.ShortUrl;
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
    private final ShortUrlHelper shortUrlHelper;


    public ShortUrl shorten(String alias, String url) {
        return shortUrlHelper.decompose(url)
                .flatMap(item -> shortUrlService.find(aliasService.find(item.getAlias()), item.getShortcut()))
                .orElseGet(() -> shortUrlService.findOrSave(aliasService.find(alias), urlService.findOrSave(url)));
    }

}
