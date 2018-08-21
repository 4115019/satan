package com.satan.common.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

/**
 * Created by huangpin on 17/3/16.
 */
@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface Name {
    boolean allowEmpty() default true;

    String message() default "is not a name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
