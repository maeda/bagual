package io.maeda.apps.bagual.repositories;

import io.maeda.apps.bagual.models.Company;
import io.maeda.apps.bagual.models.ShortUrl;
import io.maeda.apps.bagual.models.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
    Optional<ShortUrl> findByCompanyAndAliasNameAndUrl(Company company, String alias, Url url);
    Optional<ShortUrl> findByCompanyAndAliasNameAndShortcutAndDeletedIsNull(Company company, String aliasName, String shortcut);

    @Modifying
    @Query("update #{#entityName} s set s.deleted = CURRENT_TIMESTAMP where s = :shortUrl")
    void deactivate(@Param("shortUrl") ShortUrl shortUrl);
}
