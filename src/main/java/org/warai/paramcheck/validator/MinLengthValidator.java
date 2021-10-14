package org.warai.paramcheck.validator;

import com.alibaba.fastjson.JSONArray;
import org.warai.paramcheck.util.ObjectUtils;
import org.warai.paramcheck.annotation.MinLength;
import org.warai.paramcheck.constant.ErrorMessage;


/**
 * @Auther: わらい
 * @Time: 2021/9/29 15:12
 */
public class MinLengthValidator extends ParamCheckValidator<MinLength>{

    @Override
    public boolean invalid(Object value) {
        // 分组检验判断
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
        super.setFailMsg(annotation.msg());
        // 数组校验长度
        if (value instanceof JSONArray) {
            return ((JSONArray) value).size() < annotation.value();
        }

        // 字符串校验长度
        return value.toString().length() < annotation.value();
    }
}
