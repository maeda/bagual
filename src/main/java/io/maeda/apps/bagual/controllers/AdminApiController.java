package io.maeda.apps.bagual.controllers;

import io.maeda.apps.bagual.exceptions.AuthenticationException;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.models.Url;
import io.maeda.apps.bagual.services.AliasService;
import io.maeda.apps.bagual.services.PhishingService;
import io.maeda.apps.bagual.services.ShortUrlService;
import io.maeda.apps.bagual.services.UrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Optional;

@Slf4j
@RestController
@RequestScope
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminApiController {

    @Value("${io.maeda.bagual.alias:bagu.al}")
    private String defaultAlias;

    @Value("${io.maeda.apps.bagual.auth_token}")
    private String authToken;

    private final PhishingService phishingService;

    private final ShortUrlService shortUrlService;

    private final AliasService aliasService;

    private final UrlService urlService;

    @RequestMapping(value = "/api/phishing/{alias}/{shortcut}", method = RequestMethod.DELETE)
    public ResponseEntity<?> setUrlAsPhishing(
            @PathVariable("alias") String alias,
            @PathVariable("shortcut") String shortcut,
            @RequestHeader("Authorization") String authTokenHeader) {

        Optional.ofNullable(authTokenHeader)
                .filter(item -> item.equals(authToken))
                .orElseThrow(() -> new AuthenticationException(HttpStatus.UNAUTHORIZED.toString()));

       String response = Optional.ofNullable(alias)
                .map(aliasService::find)
                .flatMap(item -> this.shortUrlService.find(item, shortcut))
                .map(ShortUrl::getUrl)
                .map(Url::getOriginalUrl)
                .flatMap(urlService::setUrlAsMalicious)
                .map(Url::getOriginalUrl)
                .orElse("NOT_FOUND");

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/api/config/security/phishing/load")
    public ResponseEntity<?> loadPhishingData() {
        phishingService.process();
        return ResponseEntity.ok().build();
    }
}
