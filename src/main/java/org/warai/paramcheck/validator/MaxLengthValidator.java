package org.warai.paramcheck.validator;

import com.alibaba.fastjson.JSONArray;
import org.warai.paramcheck.util.ObjectUtils;
import org.warai.paramcheck.annotation.MaxLength;
import org.warai.paramcheck.constant.ErrorMessage;



/**
 * @Auther: わらい
 * @Time: 2021/9/26 14:09
 * <p>
 * 作用范围：
 * 1、校验字符串的长度
 * 2、校验容器类型的元素个数（List 、Object[]）
 */
public class MaxLengthValidator extends ParamCheckValidator<MaxLength> {

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

        super.setFailMsg(annotation.msg());
        // 数组校验长度
        if (value instanceof JSONArray) {
            return ((JSONArray) value).stream().anyMatch(arr -> arr.toString().length() > annotation.value());
        }

        // 字符串校验长度
        return value.toString().length() > annotation.value();
    }
}
