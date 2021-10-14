package org.warai.paramcheck.validator.chain;


import org.warai.paramcheck.util.ObjectUtils;
import org.warai.paramcheck.constant.Operator;

/**
 * @Auther: わらい
 * @Time: 2020/10/20 14:45
 */
public class OrOperatorValidator extends AbstractOperatorValidator{

    OrOperatorValidator() {
        super.operStr = Operator.OR;
    }

    @Override
    protected boolean valid(Object value, String length) {
        return !ObjectUtils.isEmpty(value);
    }
}
