package io.maeda.apps.bagual.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.maeda.apps.bagual.models.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Getter
@Builder
public class Geolocation implements Coordinate {
    private String ip;
    private String city;
    private String countryCode;
    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("lon")
    private Double longitude;
}
