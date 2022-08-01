package com.lk.common.core.util;


import cn.hutool.core.collection.CollUtil;
import com.lk.common.core.exception.CheckedException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 校验工具类 校验spring注解的参数
 * 在内部通过该工具类可以校验hibernate-validator
 */
public class ValidateUtils {
    public static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 校验参数核心方法
     */
    public static R validate(Object object) {
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object, new Class[0]);
        ConstraintViolation<Object> constraintViolation = CollUtil.getFirst(constraintViolations);
        if (constraintViolation != null) {
            return R.failed(constraintViolation.getMessage());
        }
        return R.ok();
    }

    public static void validateThrowError(Object object) {
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object, new Class[0]);
        ConstraintViolation<Object> constraintViolation = CollUtil.getFirst(constraintViolations);
        if (constraintViolation != null) {
            throw new CheckedException(constraintViolation.getMessage());
        }
    }
}
