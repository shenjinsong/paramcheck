package org.warai.paramcheck.chain;

import org.apache.commons.lang3.math.NumberUtils;
import org.warai.paramcheck.Operator;

/**
 * @Auther: わらい
 * @Time: 2021/01/04 15:21
 */
public class GreaterThanEqualOperatorValidator extends AbstractOperatorValidator {

    GreaterThanEqualOperatorValidator() {
        super.operStr = Operator.GREATER_THAN_EQUAL;
    }

    @Override
    protected boolean valid(Object param, String value) {
        if (!NumberUtils.isCreatable(param.toString()) || !NumberUtils.isCreatable(value)) {
            return false;
        }
        Double paramVal = NumberUtils.createDouble(param.toString());
        Double checkVal = NumberUtils.createDouble(value);
        return paramVal > checkVal || paramVal.equals(checkVal);
    }
}
