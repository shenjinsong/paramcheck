package com.warai.paramcheck.chain;

import com.warai.paramcheck.Operator;
import org.springframework.util.ObjectUtils;

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
