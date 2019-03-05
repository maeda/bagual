package io.maeda.apps.bagual.repositories;

import io.maeda.apps.bagual.models.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {

    //TODO it's returning the first one because there are more than one url on database. Such thing will be removed as soon as duplicated urls problem be resolved.
    Optional<Url> findFirstByOriginalUrl(String originalUrl);
}
