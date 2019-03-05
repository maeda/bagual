package io.maeda.apps.bagual.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotAShortUrlValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotAShortUrl {
    String message() default "io.maeda.apps.bagual.urls.shortUrlNotAllowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
