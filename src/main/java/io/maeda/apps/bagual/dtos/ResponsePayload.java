package io.maeda.apps.bagual.dtos;

import lombok.*;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class ResponsePayload<T> {
    private String code;
    private String message;
    private T content;
}
