package io.maeda.apps.bagual.services;

import io.maeda.apps.bagual.dtos.TopLocation;
import io.maeda.apps.bagual.models.Redirect;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.repositories.RedirectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedirectService {
    private final RedirectRepository redirectRepository;

    public int clicks(@NotNull ShortUrl url) {
        return redirectRepository.findAllByShortUrl(url).size();
    }

    public Collection<TopLocation> topLocation(ShortUrl url) {
        return redirectRepository.topLocations(url);
    }

    public Collection<TopLocation> topLocation(String alias) {
        return redirectRepository.topLocations(alias);
    }

    public Redirect save(@Valid Redirect redirect) {
        return redirectRepository.save(redirect);
    }
}
