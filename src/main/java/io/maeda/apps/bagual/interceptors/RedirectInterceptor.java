package io.maeda.apps.bagual.interceptors;

import io.maeda.apps.bagual.models.Redirect;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.services.AliasService;
import io.maeda.apps.bagual.services.GeolocationService;
import io.maeda.apps.bagual.services.RedirectService;
import io.maeda.apps.bagual.services.ShortUrlService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedirectInterceptor implements HandlerInterceptor {
    private final RedirectService redirectService;
    private final AliasService aliasService;
    private final ShortUrlService shortUrlService;
    private final GeolocationService geolocationService;
    private final Clock clock;

    @Value("${io.maeda.bagual.alias:bagu.al}")
    private String defaultAlias;

    @Value("${io.maeda.apps.bagual.redirect.alias-blacklist:11}")
    private String aliasBlacklist;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object)
            throws Exception {
        if(!HttpMethod.GET.matches(request.getMethod())) {
            return true;
        }

        Optional<ShortUrl> shortUrl = shortUrlService.find(aliasService.find(defaultAlias), request.getRequestURI().substring(1));

        shortUrl.filter(item -> !aliasBlacklist.equals(item.getShortcut()))
                .map(item -> redirectService.save(
                        Redirect.builder()
                                .shortUrl(item)
                                .httpReferer(Optional.ofNullable(request.getHeader(HttpHeaders.REFERER)).orElse(""))
                                .remoteAddr(request.getRemoteAddr())
                                .httpUserAgent(request.getHeader(HttpHeaders.USER_AGENT))
                                .requestTime(Redirect.getRequestTimeCalculated(LocalDateTime.now(clock)))
                                .redirectStatus(String.valueOf(HttpStatus.OK.value()))
                                .build()))
                .map(item -> geolocationService.record(item, request.getRemoteAddr()));

        return shortUrl.map(item -> keepHandlingInterceptors(item.getShortUrl(), item.getUrl().getOriginalUrl(), response)).orElse(Boolean.TRUE);
    }

    @SneakyThrows
    private boolean keepHandlingInterceptors(String shortUrl, String destination, HttpServletResponse response) {
        log.info(String.format("Redirecting %s", shortUrl));
        response.sendRedirect(destination);

        response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());

        return false;
    }

}
