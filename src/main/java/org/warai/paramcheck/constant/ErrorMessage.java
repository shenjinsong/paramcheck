package org.warai.paramcheck.constant;

/**
 * @Auther: わらい
 * @Time: 2021/9/28 11:44
 */
public interface ErrorMessage {

    String PARAM_ERROR = "参数不符合预期";

    String PARAM_CANNOT_EMPTY = "参数不能为空";

    String FIELD_TYPES_NOT_MATCH = "字段类型不符合预期";

    String DIGITAL_DECIMAL_PLACE_MAX = "小数位超出最大位数";

    String OVER_MAX_LENGTH = "超出最大长度限制";

    String BELOW_MIN_LENGTH ="长度不符合预期";

    String BELOW_MIN_VALUE = "低于最小值限制";

    String CHARACTER_LENGTH_NOT_MATCH = "字符长度不符合预期";

    String FORMAT_NOT_MATCH = "格式不匹配";

    String ELEMENT_OVER_MAX_LENGTH = "元素超出最大长度限制";

    String OVER_MAX_ELEMENT_SIZE = "元素超出最大个数限制";

    String OVER_MAX_VALUE = "超出最大值限制";

    String DATE_IS_PAST = "日期须比当前日期早";

    String DATE_IS_FUTURE = "日期须比当前日期晚";

    String DATA_FORMAT_ERROR = "时间格式错误";
}
