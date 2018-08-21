package com.satan.common.web.result.enums;

import com.satan.common.enums.BaseIntEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by huangpin on 17/3/16.
 */
@AllArgsConstructor
public enum TestEnum implements BaseIntEnum {
    TEST(1,"test"),
    ;

    @Getter
    private final int value;

    @Getter
    private final String name;
}
