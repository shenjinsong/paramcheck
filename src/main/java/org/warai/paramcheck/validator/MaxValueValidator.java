package org.warai.paramcheck.validator;


import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.ObjectUtils;
import org.warai.paramcheck.annotation.MaxValue;
import org.warai.paramcheck.constant.ErrorMessage;

import java.math.BigDecimal;


/**
 * @Auther: わらい
 * @Time: 2021/9/26 13:56
 * <p>
 * 作用范围：
 * 1、数字类型限制最大值
 * 2、非数字类型校验
 */
public class MaxValueValidator extends ParamCheckValidator<MaxValue> {


    @Override
    public boolean invalid(Object value) {

        if (!super.needCheck(annotation.groups())) {
            return false;
        }

        // 根据参数做非空判断
        if (ObjectUtils.isEmpty(value)){
            if (annotation.nullable()){
                return false;
            }else {
                super.setFailMsg(ErrorMessage.PARAM_CANNOT_EMPTY);
                return true;
            }
        }

        if (!NumberUtils.isCreatable(value.toString())) {
            super.setFailMsg(ErrorMessage.FIELD_TYPES_NOT_MATCH);
            return true;
        }

        BigDecimal number = NumberUtils.createBigDecimal(value.toString());
        if (annotation.decimalPlaces() != -1 && number.scale() > annotation.decimalPlaces()){
            super.setFailMsg(ErrorMessage.DIGITAL_DECIMAL_PLACE_MAX);
            return true;
        }

        super.setFailMsg(annotation.msg());
        return number.compareTo(NumberUtils.createBigDecimal(annotation.value() + "")) > 0;
    }
}
