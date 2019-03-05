package io.maeda.apps.bagual.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class Report{
    private String shortUrl;
    private String originalUrl;
    private LocalDateTime created;
    @ToString.Exclude
    private Collection<TopLocation> topLocation;
    private long clicks;
}