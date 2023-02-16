package org.warai.paramcheck.constant;

import java.util.regex.Pattern;

/**
 * @Auther: わらい
 * @Time: 2020/10/20 14:49
 */
public interface Operator {
    String BAD_VALUE = "null|undefined";       // 错误值
    String OPR_EXPS = "[<>=~|]";               // 操作符正则
    String OPR_EXPS_4_VALUE = "[<>=~]";        // 限制值校验符正则
    String OR = "|";                           // 或运算符
    String MAX_LENGTH = "~";                   // 最大长度
    String EQUAL_LENGTH = "=";                 // 相等长度
    String LESS_THAN = "<";                    // 小于指定值
    String GREATER_THAN = ">";                 // 大于指定值
    Pattern ERROR_VALUE = Pattern.compile(Operator.BAD_VALUE);
    Pattern OPR = Pattern.compile(Operator.OPR_EXPS);
    Pattern OPR_4_VALUE = Pattern.compile(Operator.OPR_EXPS_4_VALUE);
}
