package io.maeda.apps.bagual.repositories;

import io.maeda.apps.bagual.dtos.TopLocation;
import io.maeda.apps.bagual.models.Redirect;
import io.maeda.apps.bagual.models.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface RedirectRepository extends JpaRepository<Redirect, Long> {

    <T> Collection<T> findAllByShortUrl(ShortUrl shortUrl);

    @Query(value = "select new io.maeda.apps.bagual.dtos.TopLocation(r.country, r.city, r.coordinates, count(r)) from io.maeda.apps.bagual.models.Redirect r where r.shortUrl = :url and r.city is not null and r.city <> '' group by r.country, r.city, r.coordinates order by 4 desc")
    Collection<TopLocation> topLocations(@Param("url") ShortUrl url);

    @Query(value = "select new io.maeda.apps.bagual.dtos.TopLocation(r.country, r.city, r.coordinates, count(r)) from io.maeda.apps.bagual.models.Redirect r join r.shortUrl as su where su.aliasName = :alias and r.city is not null and r.city <> '' group by r.country, r.city, r.coordinates order by 4 desc")
    Collection<TopLocation> topLocations(@Param("alias") String alias);

}
