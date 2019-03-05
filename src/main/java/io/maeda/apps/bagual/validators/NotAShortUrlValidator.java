package io.maeda.apps.bagual.validators;

import io.maeda.apps.bagual.helpers.ShortUrlHelper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotAShortUrlValidator implements ConstraintValidator<NotAShortUrl, String> {

    private ShortUrlHelper validator = new ShortUrlHelper();

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        return !validator.isAShortenedUrl(url);
    }
}
