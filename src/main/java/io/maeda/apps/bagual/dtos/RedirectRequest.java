package io.maeda.apps.bagual.dtos;

import io.maeda.apps.bagual.models.Alias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class RedirectRequest {
    @NotNull
    private Alias alias;
    @NotNull
    private String shortcut;
    @NotNull
    private String referer;
    private String userAgent;
    private String remoteAddr;
}
