package io.maeda.apps.bagual.repositories;

import io.maeda.apps.bagual.models.Redirect;
import io.maeda.apps.bagual.models.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RedirectRepository extends JpaRepository<Redirect, Long> {

    Collection<Redirect> findAllByShortUrl(ShortUrl shortUrl);
}
