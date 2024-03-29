package org.warai.paramcheck.validator;

import com.alibaba.fastjson.JSONArray;
import org.warai.paramcheck.util.ObjectUtils;
import org.warai.paramcheck.annotation.Length;
import org.warai.paramcheck.constant.ErrorMessage;



/**
 * @Auther: わらい
 * @Time: 2021/9/29 14:25
 *
 * 作用范围：
 * 1、校验字符串长度范围
 * 2、校验数组元素长度范围
 */
public class LengthValidator extends ParamCheckValidator<Length> {


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

        // 数组校验长度
        super.setFailMsg(annotation.msg());
        if (value instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) value;
            return jsonArray.stream().anyMatch(arr -> arr.toString().length() < annotation.min() || arr.toString().length() > annotation.max());
        }

        int length = value.toString().length();
        return length < annotation.min() || length > annotation.max();
    }
}
