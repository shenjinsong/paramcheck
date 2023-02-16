package org.warai.paramcheck.validator.chain;

import com.alibaba.fastjson.JSON;
import org.springframework.util.ObjectUtils;
import org.warai.paramcheck.constant.Operator;


/**
 * @Auther: わらい
 * @Time: 2020/10/20 10:53
 */
public abstract class AbstractOperatorValidator {

    String operStr;
    private AbstractOperatorValidator nextOperatorValidator;


    public boolean inspectPass(Object param, String value, String operStr){

        if (ObjectUtils.nullSafeEquals(this.operStr, operStr)) {

            if (Operator.ERROR_VALUE.matcher(JSON.toJSONString(param).toLowerCase()).find()) {
                return false;
            }
            return valid(param, value);
        } else {
            if (this.nextOperatorValidator == null) {
                throw new RuntimeException("Unsupported operators :" + operStr);
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
        DefaultOperatorValidator defaultOperatorValidator = new DefaultOperatorValidator();

        orOperatorValidator.setNextOperatorValidator(equalLengthOperatorValidator);
        equalLengthOperatorValidator.setNextOperatorValidator(greaterThanOperatorValidator);
        greaterThanOperatorValidator.setNextOperatorValidator(lessThanOperatorValidator);
        lessThanOperatorValidator.setNextOperatorValidator(maxLengthOperatorValidator);
        maxLengthOperatorValidator.setNextOperatorValidator(defaultOperatorValidator);

        return orOperatorValidator;
    }

}
