package org.warai.paramcheck.validator;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.ObjectUtils;
import org.warai.paramcheck.annotation.MinValue;
import org.warai.paramcheck.constant.ErrorMessage;


/**
 * @Auther: わらい
 * @Time: 2021/9/29 15:17
 */
public class MinValueValidator extends ParamCheckValidator<MinValue> {

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

        super.setFailMsg(annotation.msg());
        return NumberUtils.createBigDecimal(value.toString()).compareTo(NumberUtils.createBigDecimal(annotation.value() + "")) < 0;
    }
}
