package io.maeda.apps.bagual.dtos;

import io.maeda.apps.bagual.models.Coordinate;
import lombok.Value;

@Value
public class TopLocation {

    String country;
    String city;
    Coordinate coordinates;
    long total;
}
