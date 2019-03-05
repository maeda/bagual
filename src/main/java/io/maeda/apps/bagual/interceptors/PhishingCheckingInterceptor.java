package io.maeda.apps.bagual.interceptors;

import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.services.PhishingService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PhishingCheckingInterceptor implements HandlerInterceptor {

    private final PhishingService phishingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        Optional<ShortUrl> phishing = phishingService.isPhishing(request.getServerName(), request.getRequestURI().substring(1));

        return phishing.map(item -> redirect(item, response)).orElse(Boolean.TRUE);
    }

    @SneakyThrows
    private boolean redirect(ShortUrl url, HttpServletResponse response) {
        response.sendRedirect(String.format("/warning?shortUrl=%s&url=%s", url.getShortUrl(), url.getUrl().getOriginalUrl()));
        log.info(String.format("Phishing url detected. ShortUrl: %s, Url: %s", url.getShortUrl(), url.getUrl().getOriginalUrl()));
        response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());

        return false;
    }
}
