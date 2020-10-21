package com.warai.paramcheck.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.warai.paramcheck.annotation.ParamCheck;
import com.warai.paramcheck.handler.chain.AbstractOperatorValidator;
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
public class FieldInspect {

    private List<String> badFields = new ArrayList<>(1);
    private boolean invalid;
    private JSONObject params;
    private ParamCheck paramCheck;
    private static AbstractOperatorValidator operatorValidatorChain = AbstractOperatorValidator.getChainOfOperatorValidator();

    private static Logger log = Logger.getLogger(FieldInspect.class.getName());

    public FieldInspect(ParamCheck paramCheck, JSONObject params) {
        this.params = params;
        this.paramCheck = paramCheck;
    }

    public void checkParam() throws BadStringOperationException {
        for (String checkStr : paramCheck.value()) {
            if (invalid(checkStr)) {
                badFields.add(checkStr);
                this.invalid = true;
                log.warning("*** Bad field : " + checkStr + " ***");
            }
        }

    }

    private boolean invalid(String checkStr){

        if (ObjectUtils.isEmpty(params)) {
            return true;
        }

        checkStr = checkStr.replaceAll(" ", "");
        log.info("校验的参数：" + checkStr);

        // 包含运算符做特殊检验
        if (Operator.OPR.matcher(checkStr).find()) {
            String[] checkStrs = checkStr.split(Operator.OPR_EXPS);

            // 包括数值限制校验：str - 4
            Matcher matcher = Operator.OPR_4_VALUE.matcher(checkStr);
            if (matcher.find()) {
                String value = checkStrs[checkStrs.length - 1];

                // 拿到操作符
                String operStr = matcher.group();
                if (matcher.find()) {
                    operStr += matcher.group();
                }
                return containErrorValue(params.get(checkStrs[0]), value, operStr);
            } else {
                // 无数值限制校验 str | str2
                if (checkStr.contains(Operator.OR)) {
                    return Arrays.stream(checkStrs).allMatch(str -> containErrorValue(params.get(str)));
                }
            }
            return true;

        } else {
            return containErrorValue(params.get(checkStr));
        }
    }

    private boolean containErrorValue(Object obj) {
        return containErrorValue(obj, null, null);
    }

    private boolean containErrorValue(Object param, String value, String operStr) {
        if (ObjectUtils.isEmpty(param)) {
            return true;
        } else if (param instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) param;
            for (Object o : jsonArray) {
                if (containErrorValue(o, value, operStr)) {
                    return true;
                }
            }
        }

        return !operatorValidatorChain.inspectPass(param, value, operStr);
    }

    public List<String> getBadFields() {
        return badFields;
    }

    public boolean isInvalid() {
        return invalid;
    }
}
