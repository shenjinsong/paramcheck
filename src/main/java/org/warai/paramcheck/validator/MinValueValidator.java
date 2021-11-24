package org.warai.paramcheck.validator;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.math.NumberUtils;
import org.warai.paramcheck.util.ObjectUtils;
import org.warai.paramcheck.annotation.MinValue;
import org.warai.paramcheck.constant.ErrorMessage;

import java.math.BigDecimal;


/**
 * @Auther: わらい
 * @Time: 2021/9/29 15:17
 */
public class MinValueValidator extends ParamCheckValidator<MinValue> {

    @Override
    public boolean invalid(Object value) {

        // 根据参数做非空判断
        if (ObjectUtils.isEmpty(value)){
            if (annotation.nullable()){
                return false;
            }else {
                super.setFailMsg(ErrorMessage.PARAM_CANNOT_EMPTY);
                return true;
            }
        }

        if (value instanceof JSONArray){
            return ((JSONArray) value).stream().anyMatch(this::numberCheck);
        }

       return numberCheck(value);
    }

    @Override
    public boolean numberCheck(Object value) {
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
        return number.compareTo(NumberUtils.createBigDecimal(annotation.value() + "")) < 0;

    }
}
