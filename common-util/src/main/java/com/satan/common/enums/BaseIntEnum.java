package com.satan.common.enums;

import java.io.Serializable;

/**
 * Created by huangpin on 17/3/16.
 */
public interface BaseIntEnum extends Serializable {
    long serialVersionUID = 1l;

    int getValue();
    String getName();
}
