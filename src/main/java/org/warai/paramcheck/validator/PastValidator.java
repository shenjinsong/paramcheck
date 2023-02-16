package org.warai.paramcheck.validator;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.warai.paramcheck.util.ObjectUtils;
import org.warai.paramcheck.annotation.Past;
import org.warai.paramcheck.constant.ErrorMessage;



import java.text.ParseException;
import java.util.Date;


/**
 * @Auther: わらい
 * @Time: 2021/9/29 10:24
 */
public class PastValidator extends ParamCheckValidator<Past> {

    @Override
    public boolean invalid(Object value) {

        // 根据参数做非空判断
        if (ObjectUtils.isEmpty(value)){
            if (annotation.nullable()){
                return false;
            }else {
                super.setFailMsg(ErrorMessage.PARAM_CANNOT_EMPTY);
                return true;
            }
        }

        Date timeParam;
        // 解析时间
        if(ObjectUtils.isEmpty(annotation.format())){
            // 未指定时间格式，默认按照13位时间戳来转换
            if (value.toString().length() != 13 || !NumberUtils.isCreatable(value.toString())) {
                super.setFailMsg(ErrorMessage.DATA_FORMAT_ERROR);
                return true;
            }
            timeParam = new Date(NumberUtils.createLong(value.toString()));
        }else { // 按照指定的时间格式来解析时间
            try {
                timeParam = DateUtils.parseDate(value.toString(), annotation.format());
            } catch (ParseException e) {
                super.setFailMsg(ErrorMessage.DATA_FORMAT_ERROR);
                return true;
            }
        }

        Date time = new Date();
        time = DateUtils.setHours(time, 0);
        time = DateUtils.setMinutes(time, 0);
        time = DateUtils.setSeconds(time, 0);
        time = DateUtils.setMilliseconds(time, 0);
        time = DateUtils.addDays(time, annotation.value());

        super.setFailMsg(annotation.msg());
        return timeParam.getTime() > time.getTime();
    }

}
