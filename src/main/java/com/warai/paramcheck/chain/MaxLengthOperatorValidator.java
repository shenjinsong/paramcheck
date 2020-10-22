package com.warai.paramcheck.chain;

import com.warai.paramcheck.Operator;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @Auther: わらい
 * @Time: 2020/10/20 14:57
 */
public class MaxLengthOperatorValidator extends AbstractOperatorValidator{

    MaxLengthOperatorValidator() {
        super.operStr = Operator.MAX_LENGTH;
    }

    @Override
    protected boolean valid(Object param, String value) {
        if (!NumberUtils.isCreatable(value)) {
            log.warning("Check failure : param" + param + ", limiting condition : " + operStr + " " + value);
            return false;
        }
        return param.toString().length() <= NumberUtils.createInteger(value);
    }
}
