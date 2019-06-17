package io.maeda.apps.bagual.repositories;

import io.maeda.apps.bagual.models.Company;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.models.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByCompanyAndAliasNameAndUrl(Company company, String alias, Url url);
    Optional<ShortUrl> findByCompanyAndAliasNameAndShortcut(Company company, String aliasName, String shortcut);
}
