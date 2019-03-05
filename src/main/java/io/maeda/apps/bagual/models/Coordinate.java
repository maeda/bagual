package io.maeda.apps.bagual.models;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Coordinate {
    Double getLatitude();
    Double getLongitude();
}
