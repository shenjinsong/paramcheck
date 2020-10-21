package com.warai.paramcheck.handler.chain;

import com.warai.paramcheck.handler.Operator;
import org.apache.commons.lang3.math.NumberUtils;


/**
 * @Auther: わらい
 * @Time: 2020/10/20 14:58
 */
public class EqualLengthOperatorValidator extends AbstractOperatorValidator {

    EqualLengthOperatorValidator() {
        super.operStr = Operator.EQUAL_LENGTH;
    }

    @Override
    protected boolean valid(Object param, String value) {

        if (!NumberUtils.isCreatable(value)) {
            log.warning("Check failure : param" + param + ", limiting condition : " + operStr + " " + value);
            return false;
        }

        return param.toString().length() == NumberUtils.createInteger(value);
    }

}
