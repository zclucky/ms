package com.ms.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


@Component
public class ValidatorImp implements InitializingBean {

    private Validator validator;

    // 实现校验方法 并返回校验结果
    public ValidatorResult validate(Object bean){
        final ValidatorResult result = new ValidatorResult();
        Set<ConstraintViolation<Object>> constraintViolations =  validator.validate(bean);
        if (constraintViolations.size() > 0){
            // 如果有错误
            result.setHasErrors(true);
            constraintViolations.forEach(constraintViolation->{
                String errMsg = constraintViolation.getMessage();
                String propertyName = constraintViolation.getPropertyPath().toString();
                result.getErrMsgMap().put(propertyName,errMsg);
            });
        }
        return result;
    }

    @Override
    public void afterPropertiesSet() {
        // 将hibernate validator 通过工厂方法的初始化方法使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
