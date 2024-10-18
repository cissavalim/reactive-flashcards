package br.com.dio.reactiveflashcards.config.validation;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Slf4j
public class MongoIdValidator implements ConstraintValidator<MongoId, String> {

    @Override
    public void initialize(final MongoId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String value, ConstraintValidatorContext constraintValidatorContext) {
        log.info("validating-mongo-id={}", value);
        return StringUtils.isNotBlank(value) && ObjectId.isValid(value);
    }
}
