package org.warai.paramcheck.chain;

import org.warai.paramcheck.Operator;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * @Auther: わらい
 * @Time: 2020/10/20 15:00
 */
public class LessThanOperatorValidator extends AbstractOperatorValidator {

    LessThanOperatorValidator() {
        super.operStr = Operator.LESS_THAN;
    }

    @Override
    protected boolean valid(Object param, String value) {
        if (!NumberUtils.isCreatable(param.toString()) || !NumberUtils.isCreatable(value)) {
//            log.warning("Check failure : param : " + param + ", limiting condition : " + operStr + " " + value);
            return false;
        }
        return NumberUtils.createDouble(param.toString()) < NumberUtils.createDouble(value);
    }
}
