package org.warai.paramcheck.validator.chain;


import org.apache.commons.lang3.math.NumberUtils;
import org.warai.paramcheck.constant.Operator;

/**
 * @Auther: わらい
 * @Time: 2020/10/20 17:04
 */
public class GreaterThanOperatorValidator extends AbstractOperatorValidator {

    GreaterThanOperatorValidator() {
        super.operStr = Operator.GREATER_THAN;
    }

    @Override
    protected boolean valid(Object param, String value) {
        if (!NumberUtils.isCreatable(param.toString()) || !NumberUtils.isCreatable(value)) {
//            log.warning("Check failure : param" + param + ", limiting condition : " + operStr + " " + value);
            return false;
        }
        return NumberUtils.createDouble(param.toString()) > NumberUtils.createDouble(value);
    }

}
