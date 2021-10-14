package org.warai.paramcheck.validator;

import org.warai.paramcheck.util.ObjectUtils;
import org.warai.paramcheck.annotation.NonNull;


/**
 * @Auther: わらい
 * @Time: 2021/9/26 14:03
 */
public class NonNullValidator extends ParamCheckValidator<NonNull> {

    @Override
    public boolean invalid(Object value) {

        if (!super.needCheck(annotation.groups())) {
            return false;
        }

        super.setFailMsg(annotation.msg());
        return ObjectUtils.isEmpty(value);
    }
}
