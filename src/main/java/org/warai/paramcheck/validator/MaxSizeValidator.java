package org.warai.paramcheck.validator;


import com.alibaba.fastjson.JSONArray;
import org.warai.paramcheck.util.ObjectUtils;
import org.warai.paramcheck.annotation.MaxSize;
import org.warai.paramcheck.constant.ErrorMessage;


/**
 * @Auther: わらい
 * @Time: 2021/9/26 11:00
 * <p>
 * 作用范围：
 * 1、限制List或Object[]的元素个数
 */
public class MaxSizeValidator extends ParamCheckValidator<MaxSize> {

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
        if (value instanceof JSONArray) {
            return ((JSONArray) value).size() > annotation.value();
        }

        return true;
    }
}
