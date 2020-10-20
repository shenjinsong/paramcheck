package com.warai.paramcheck.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.warai.paramcheck.annotation.ParamCheck;
import org.springframework.util.ObjectUtils;

import javax.management.BadStringOperationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: わらい
 * @Time: 2020/10/16 17:36
 */
public class FieldCheck {

    private List<String> badFields = new ArrayList<>(1);
    private boolean invalid;
    private JSONObject params;
    private ParamCheck paramCheck;

    public FieldCheck(ParamCheck paramCheck, JSONObject params) {
        this.params = params;
        this.paramCheck = paramCheck;
    }

    private static Logger log = Logger.getLogger(FieldCheck.class.getName());

    private final static String
            BAD_VALUE = "null|undefined",       // 错误值
            OPR_EXPS = "[<>=~|]",           // 操作符正则
            OPR_EXPS_4_VALUE = "[<>=~]",    // 限制值校验符正则
            OR = "|",                           // 或运算符
            MAX_LENGTH = "~",                   // 最大长度
            EQUAL_LENGTH = "=",                 // 相等长度
            LESS_THAN = "<",                    // 小于指定值
            GREATER_THAN = ">";                 // 大于指定值

    private static Pattern ERROR_VALUE = Pattern.compile(BAD_VALUE);
    private static Pattern OPR = Pattern.compile(OPR_EXPS);
    private static Pattern OPR_4_VALUE = Pattern.compile(OPR_EXPS_4_VALUE);

    public void checkParam() throws BadStringOperationException {
        for (String checkStr : paramCheck.value()) {
            if (invalid(checkStr)) {
                badFields.add(checkStr);
                this.invalid = true;
                log.warning("*** Bad field : " + checkStr + " ***");
            }
        }

    }

    private boolean invalid(String checkStr) throws BadStringOperationException {

        if (params == null){
            return true;
        }

        checkStr = checkStr.replaceAll(" ", "");
        log.info("校验的参数：" + checkStr);

        // 包含运算符做特殊检验
        if (OPR.matcher(checkStr).find()) {
            String[] checkStrs = checkStr.split(OPR_EXPS);

            // 包括数值限制校验：str - 4
            Matcher matcher = OPR_4_VALUE.matcher(checkStr);
            if (matcher.find()) {
                // 拿到校验字段的限制数值
                String field = checkStrs[0];
                Integer length;
                try {
                    length = Integer.valueOf(checkStrs[checkStrs.length - 1]);
                } catch (Exception e) {
                    log.warning("*** @ParamCheck Incorrect use of restricted characters, Check invalid [" + checkStr + "] ***");
                    throw new BadStringOperationException("@ParamCheck : " + checkStr);
                }

                // 拿到操作符
                String operStr = matcher.group();
                if (matcher.find()){
                    operStr+=matcher.group();
                }

                return containErrorValue(params.get(field), length, operStr);

            } else {
                // 无数值限制校验 str | str2
                if (checkStr.contains(OR)) {
                    // field1 | field2 => {"field1", "field2"}
                    return Arrays.stream(checkStrs).allMatch(str -> containErrorValue(params.get(str)));
                }
            }
            return true;

        } else {
            return containErrorValue(params.get(checkStr));
        }
    }

    private static boolean containErrorValue(Object obj) {
        return containErrorValue(obj, null, null);
    }

    private static boolean containErrorValue(Object obj, Integer length, String operStr) {
        if (ObjectUtils.isEmpty(obj)) {
            log.warning("*** Bad parameter: " + JSON.toJSONString(obj) + (length == null ? "" : ", length : " + length) + " ***");
            return true;
        } else if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o : jsonArray) {
                if (containErrorValue(o, length, operStr)) {
                    return true;
                }
            }
        }

        if (ERROR_VALUE.matcher(JSON.toJSONString(obj).toLowerCase()).find()) {
            return true;
        }

        // TODO 其它操作符
        //              EQUAL_LENGTH = "_",               // 相等长度
        //            EQUAL_VALUE = "=",                  // 相等指定值
        //            LESS_THAN = "<",                    // 小于指定值
        //            GREATER_THAN = ">";

        if (OR.equals(operStr) || length == null) {
            return false;
        }

        return obj.toString().length() > length;
    }

    public List<String> getBadFields() {
        return badFields;
    }

    public boolean isInvalid() {
        return invalid;
    }
}
