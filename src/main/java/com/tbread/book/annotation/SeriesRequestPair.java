package com.tbread.book.annotation;

import com.tbread.book.validate.SeriesRequestPairValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SeriesRequestPairValidator.class)
public @interface SeriesRequestPair {
    String message() default "시리즈일경우 포함될 시리즈를 반드시 선택해야합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
