package com.warai.paramcheck.handler.chain;

import com.alibaba.fastjson.JSON;
import com.warai.paramcheck.handler.Operator;

import java.util.logging.Logger;

/**
 * @Auther: わらい
 * @Time: 2020/10/20 10:53
 */
public abstract class AbstractOperatorValidator {

    String operStr;
    private AbstractOperatorValidator nextOperatorValidator;
    static Logger log = Logger.getLogger(AbstractOperatorValidator.class.getName());

    public boolean inspectPass(Object param, String value, String operStr) {
        log.info("Current validator" + this.getClass().getName());
        if (this.operStr.equals(operStr)) {
            log.info(this.getClass().getName() + "to verify : " + operStr);
            if (Operator.ERROR_VALUE.matcher(JSON.toJSONString(param).toLowerCase()).find()) {
                return false;
            }
            return valid(param, value);
        } else {
            if (this.nextOperatorValidator == null) {
                return false;
            }
            return this.nextOperatorValidator.inspectPass(param, value, operStr);
        }
    }


    void setNextOperatorValidator(AbstractOperatorValidator nextOperatorValidator) {
        this.nextOperatorValidator = nextOperatorValidator;
    }

    protected abstract boolean valid(Object param, String value);


    public static AbstractOperatorValidator getChainOfOperatorValidator() {

        OrOperatorValidator orOperatorValidator = new OrOperatorValidator();
        EqualLengthOperatorValidator equalLengthOperatorValidator = new EqualLengthOperatorValidator();
        GreaterThanOperatorValidator greaterThanOperatorValidator = new GreaterThanOperatorValidator();
        LessThanOperatorValidator lessThanOperatorValidator = new LessThanOperatorValidator();
        MaxLengthOperatorValidator maxLengthOperatorValidator = new MaxLengthOperatorValidator();

        orOperatorValidator.setNextOperatorValidator(equalLengthOperatorValidator);
        equalLengthOperatorValidator.setNextOperatorValidator(greaterThanOperatorValidator);
        greaterThanOperatorValidator.setNextOperatorValidator(lessThanOperatorValidator);
        lessThanOperatorValidator.setNextOperatorValidator(maxLengthOperatorValidator);

        return orOperatorValidator;
    }

}
