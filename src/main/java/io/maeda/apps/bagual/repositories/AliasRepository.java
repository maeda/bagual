package io.maeda.apps.bagual.repositories;

import io.maeda.apps.bagual.models.Alias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AliasRepository extends JpaRepository<Alias, Long> {

    Optional<Alias> findByAliasName(String alias);
}
