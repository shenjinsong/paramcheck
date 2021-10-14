package org.warai.paramcheck.validator.chain;

import org.warai.paramcheck.util.ObjectUtils;

/**
 * @Auther: わらい
 * @Time: 2020/10/22 14:57
 */
public class DefaultOperatorValidator extends AbstractOperatorValidator {

    DefaultOperatorValidator() {
        super.operStr = null;
    }

    @Override
    protected boolean valid(Object param, String value) {
        return !ObjectUtils.isEmpty(param);
    }
}
