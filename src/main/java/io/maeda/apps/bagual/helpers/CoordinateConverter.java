package io.maeda.apps.bagual.helpers;

import io.maeda.apps.bagual.dtos.Geolocation;
import io.maeda.apps.bagual.models.Coordinate;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CoordinateConverter implements AttributeConverter<Coordinate, String> {

    private static final String SEPARATOR = "\\|";

    @Override
    public String convertToDatabaseColumn(Coordinate attribute) {
        if(attribute == null) {
            return null;
        }
        return attribute.getLatitude() + "|" + attribute.getLongitude();
    }

    @Override
    public Coordinate convertToEntityAttribute(String dbData) {
        if(StringUtils.isEmpty(dbData)) {
            return null;
        }

        String[] coordinates = dbData.split(SEPARATOR);

        return Geolocation.builder()
                .latitude(Double.valueOf(coordinates[0]))
                .longitude(Double.valueOf(coordinates[1]))
                .build();
    }
}
