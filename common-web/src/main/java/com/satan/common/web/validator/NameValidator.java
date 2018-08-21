package com.satan.common.web.validator;

import com.satan.common.utils.FieldUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by huangpin on 17/3/16.
 */
public class NameValidator implements ConstraintValidator<Name, String> {

    boolean allowEmpty = false;

    @Override
    public void initialize(Name name) {
        allowEmpty = name.allowEmpty();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return (allowEmpty && StringUtils.isEmpty(s)) || (StringUtils.isNotEmpty(s) && FieldUtil.isName(s));
    }
}
