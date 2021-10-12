package org.warai.paramcheck.validator;

import com.alibaba.fastjson.JSONArray;
import org.springframework.util.ObjectUtils;
import org.warai.paramcheck.annotation.ElementLength;
import org.warai.paramcheck.constant.ErrorMessage;



/**
 * @Auther: わらい
 * @Time: 2021/9/29 14:58
 */
public class ElementLengthValidator extends ParamCheckValidator<ElementLength> {

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

        if (!(value instanceof JSONArray)) {
            super.setFailMsg(ErrorMessage.FIELD_TYPES_NOT_MATCH);
            return true;
        }

        super.setFailMsg(annotation.msg());
        JSONArray jsonArray = (JSONArray) value;
        for (Object val : jsonArray) {
            if (val instanceof String) {
                int length = ((String) val).length();
                if (length < annotation.min() || length > annotation.max()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }
}
