package io.maeda.apps.bagual.services;

import io.maeda.apps.bagual.dtos.Report;
import io.maeda.apps.bagual.models.ShortUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportService {
    private final RedirectService redirectService;
    private final ShortUrlService shortUrlService;
    private final AliasService aliasService;

    public Optional<Report> details(String alias, String shortcut) {

        Optional<ShortUrl> shortUrl = shortUrlService.find(aliasService.find(alias), shortcut);
        return shortUrl.map(item ->
                Report.builder()
                        .shortUrl(item.getShortUrl())
                        .originalUrl(item.getUrl().getOriginalUrl())
                        .created(item.getCreated())
                        .clicks(redirectService.clicks(item)).build()
        );
    }

}
