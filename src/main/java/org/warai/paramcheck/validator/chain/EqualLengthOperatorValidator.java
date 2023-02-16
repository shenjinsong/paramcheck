package org.warai.paramcheck.validator.chain;

import org.apache.commons.lang3.math.NumberUtils;
import org.warai.paramcheck.constant.Operator;


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
//            log.warning("Check failure : param" + param + ", limiting condition : " + operStr + " " + value);
            return false;
        }

        return param.toString().length() == NumberUtils.createInteger(value);
    }

}
