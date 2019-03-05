package io.maeda.apps.bagual.dtos;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Value
public class ShortUrlVO {
    @NotEmpty
    @NonNull
    private String alias;

    @NotEmpty
    @NonNull
    private String shortcut;
}
