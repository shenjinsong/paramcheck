package org.warai.paramcheck.validator;

import org.warai.paramcheck.annotation.ValueSet;
import org.warai.paramcheck.constant.ErrorMessage;
import org.warai.paramcheck.util.ObjectUtils;

import java.util.Arrays;

/**
 * @Auther: わらい
 * @Time: 2021/10/28 11:08
 */
public class ValueSetValidator  extends ParamCheckValidator<ValueSet>{

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
        super.setFailMsg(ErrorMessage.PARAM_ERROR);
        return Arrays.stream(annotation.value()).noneMatch(s -> s.equals(value.toString()));
    }
}
