package com.tbread.book.book.entity.enums;

import lombok.Getter;

@Getter
public enum ClassificationNumber {
    TOTAL(0),
    PHILOSOPHY(1),
    RELIGION(2),
    SOCIAL_SCIENCE(3),
    NATURAL_SCIENCE(4),
    SCIENCE_TECHNOLOGY(5),
    ART(6),
    LANGUAGE(7),
    LITERATURE(8),
    HISTORY(9);
    //십진분류 이하의 2차,3차 분류는 추가하지않음

    private final int cNum;
    private ClassificationNumber(int cNum) {
        this.cNum = cNum;
    }
}
