package io.maeda.apps.bagual.services;

import io.maeda.apps.bagual.exceptions.AliasException;
import io.maeda.apps.bagual.models.Alias;
import io.maeda.apps.bagual.repositories.AliasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AliasService {
    private final AliasRepository aliasRepository;

    public Alias find(String aliasName) {
        return aliasRepository.findByAliasName(aliasName)
                .orElseThrow(() -> new AliasException("ERROR=not_found:"+aliasName));
    }
}
