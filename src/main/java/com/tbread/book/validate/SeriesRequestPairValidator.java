package com.tbread.book.validate;

import com.tbread.book.annotation.SeriesRequestPair;
import com.tbread.book.book.dto.request.AddBookRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class SeriesRequestPairValidator implements ConstraintValidator<SeriesRequestPair, AddBookRequest> {
    @Override
    public boolean isValid(AddBookRequest request, ConstraintValidatorContext context) {
        Boolean isSeries = request.isSeries();
        if (Objects.isNull(isSeries)) {
            return true;
            //유효성은 바르지 않으나 notnull 단계에서 처리
        }
        if (isSeries && Objects.isNull(request.seriesId())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("시리즈일경우 포함될 시리즈를 반드시 선택해야합니다.").addConstraintViolation();
            return false;
        }
        return true;
    }
}
