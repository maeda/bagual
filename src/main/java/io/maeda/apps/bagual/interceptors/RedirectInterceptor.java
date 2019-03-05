package io.maeda.apps.bagual.interceptors;

import io.maeda.apps.bagual.dtos.RedirectRequest;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.services.AliasService;
import io.maeda.apps.bagual.services.RedirectService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedirectInterceptor implements HandlerInterceptor {
    private final RedirectService redirectService;
    private final AliasService aliasService;

    @Value("${io.maeda.bagual.alias:bagu.al}")
    private String defaultAlias;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object)
            throws Exception {
        if(!HttpMethod.GET.matches(request.getMethod())) {
            return true;
        }

        Optional<ShortUrl> url = redirectService.record(
                RedirectRequest.builder()
                        .alias(aliasService.find(defaultAlias))
                        .referer(Optional.ofNullable(request.getHeader("referer")).orElse(""))
                        .remoteAddr(request.getRemoteAddr())
                        .shortcut(request.getRequestURI().substring(1))
                        .userAgent(request.getHeader("User-Agent"))
                        .build()
        );

        return url.map(item -> keepHandlingInterceptors(item.getShortUrl(), item.getUrl().getOriginalUrl(), response)).orElse(Boolean.TRUE);
    }

    @SneakyThrows
    private boolean keepHandlingInterceptors(String shortUrl, String destination, HttpServletResponse response) {
        log.info(String.format("Redirecting %s", shortUrl));
        response.sendRedirect(destination);

        response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());

        return false;
    }

}
