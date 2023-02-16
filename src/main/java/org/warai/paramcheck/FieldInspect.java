package org.warai.paramcheck;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.warai.paramcheck.annotation.InnerObj;
import org.warai.paramcheck.annotation.ParamCheck;
import org.warai.paramcheck.constant.Operator;
import org.warai.paramcheck.constant.Validator;
import org.warai.paramcheck.util.ObjectUtils;
import org.warai.paramcheck.validator.ParamCheckValidator;
import org.warai.paramcheck.validator.chain.AbstractOperatorValidator;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;


/**
 * @Auther: わらい
 * @Time: 2020/10/16 17:36
 */
public class FieldInspect {
    private static Logger log = Logger.getLogger(FieldInspect.class.getName());
    private static AbstractOperatorValidator operatorValidatorChain = AbstractOperatorValidator.getChainOfOperatorValidator();
    private static Map<Class<? extends Annotation>, Method> methods;

    private Map<String, Set<String>> badFields = new HashMap<>(1);
    private Map<String, List<Annotation>> fieldInfoMap = new HashMap<>(1);

    private String SEPARATOR = ".";
    private JSONObject params;
    private ParamCheck paramCheck;
    private String currentKey;

    static {
        long l = System.currentTimeMillis();
        try {
            methods = Validator.initGroupsMethod();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("ParamCheck groups() 方法预缓存失败");
        }
        long l1 = System.currentTimeMillis();
        log.info("*** The ParamCheck Groups() method successfully preloads the cache in " + (l1 - l) + " ms ***");

    }

    public FieldInspect(ParamCheck paramCheck, JSONObject params) throws InvocationTargetException, IllegalAccessException {
        this.params = params;
        this.paramCheck = paramCheck;
        this.getAnnoInfo();
    }

    // 多层对象校验
    private void check(String key, List<Annotation> annoList, JSON params) {

        if (params != null && (key.contains(SEPARATOR) || params instanceof JSONArray)) { // 递归取参进行校验

            if (params instanceof JSONObject) {
                // 字段包含自定义对象类型
                String[] res = key.split("\\" + SEPARATOR, 2);
                String key1 = res[0];
                String key2 = res[1];
                JSONObject jsonObject = (JSONObject) params;
                params = (JSON) jsonObject.get(key1);
                // 下一层参数的key
                this.check(key2, annoList, params);
            } else {
                JSONArray jsonArray = (JSONArray) params;
                for (Object obj : jsonArray) {
                    this.check(key, annoList, (JSON) obj);
                }
            }

        } else {
            // 这里才是做校验的地方
            // 参数为null 交给验证器处理
            for (int i = 0, size = annoList.size(); i < size; i++) {
                this.objCheck(key, params, annoList.get(i));
            }
        }
    }

    private void objCheck(String key, JSON json, Annotation annotation) {
        Object value = null;
        if (json != null) {
            JSONObject jsonObject = json.toJavaObject(JSONObject.class);
            value = jsonObject.get(key);
        }
        ParamCheckValidator<Annotation> paramCheckValidator = Validator.get(annotation);
        if (paramCheckValidator.invalid(value)) {
            String desc = paramCheckValidator.getFailMsg() + " | 参数：" + value;
            if (desc.length() > 50) {
                desc = desc.substring(0, 50) + " ...";
            }
            this.setBadFields(currentKey, desc);
        }
    }


    public FieldInspect checkParam() {
        // 注解字段校验
        for (Map.Entry<String, List<Annotation>> entry : fieldInfoMap.entrySet()) {
            String key = entry.getKey();
            List<Annotation> value = entry.getValue();
            this.currentKey = key;
            check(key, value, params);
        }
        // 普通校验
        for (String checkStr : paramCheck.value()) {
            if (invalid(checkStr)) {
                checkStr = checkStr.replace(" ", "").split(Operator.OPR_EXPS)[0];
                this.setBadFields(checkStr, paramCheck.msg());
            }
        }
        return this;
    }

    private boolean invalid(String checkStr) {

        if (ObjectUtils.isEmpty(params)) {
            return true;
        }

        checkStr = checkStr.replaceAll(" ", "");

        // 包含运算符做特殊检验
        if (Operator.OPR.matcher(checkStr).find()) {
            String[] checkStrs = checkStr.split(Operator.OPR_EXPS);

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
                    for (String str : checkStrs) {
                        if (!containErrorValue(params.get(str), null, Operator.OR)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return true;

        } else {
            return containErrorValue(params.get(checkStr));
        }
    }

    private boolean containErrorValue(Object obj){
        return containErrorValue(obj, null, null);
    }

    private boolean containErrorValue(Object param, String value, String operStr){
        if (param instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) param;
            if(jsonArray.isEmpty()){
                return true;
            }
            for (Object o : jsonArray) {
                if (containErrorValue(o, value, operStr)) {
                    return true;
                }
            }
            return false;
        } else {
            return !operatorValidatorChain.inspectPass(param, value, operStr);
        }

    }

    private boolean sameGroup(String[] annoGroups) {
        // 1、无分组校验（默认需要校验）
        // 2、有分组校验且为同一组
        String[] groups = paramCheck.groups();
        if (groups.length == 0) {
            return true;
        }

        List<String> list1 = new ArrayList<>(Arrays.asList(groups));
        List<String> list2 = new ArrayList<>(Arrays.asList(annoGroups));
        list1.retainAll(list2);
        return list1.size() > 0;
    }

    private Map<String, List<Annotation>> getAnnoInfo(Class cls) throws InvocationTargetException, IllegalAccessException {
        Map<String, List<Annotation>> map = new HashMap<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            List<Annotation> annoList = new ArrayList<>();
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.getPackage().getName().contains(this.getClass().getPackage().getName())) {

                    if (annotation instanceof InnerObj) {
                        InnerObj innerObj = (InnerObj) annotation;
                        Map<String, List<Annotation>> annoInfo = getAnnoInfo(innerObj.value());
                        annoInfo.keySet().forEach(key -> map.put(field.getName() + SEPARATOR + key, annoInfo.get(key)));
                    } else {
                        Method method = methods.get(annotationType);
//                        Assert.notNull(method, annotationType + " groups() 方法不存在");
                        String[] groups = (String[]) method.invoke(annotation);
                        if (!sameGroup(groups)) {
                            continue;
                        }
                        annoList.add(annotation);
                    }
                }
            }
            if (!annoList.isEmpty()) {
                map.put(field.getName(), annoList);
            }
        }
        return map;
    }


    private void getAnnoInfo() throws InvocationTargetException, IllegalAccessException {
        // 需要校验的字段和校验注解信息
        for (Class cls : paramCheck.classes()) {
            Map<String, List<Annotation>> map = this.getAnnoInfo(cls);
            fieldInfoMap.putAll(map);
        }
    }


    Map<String, Set<String>> getBadFields() {
        return badFields;
    }

    private void setBadFields(String field, String failMsg) {
        Set<String> msg = new HashSet<>(1);
        if (badFields.containsKey(field)) {
            msg = badFields.get(field);
        }
        msg.add(failMsg);
        this.badFields.put(field, msg);
    }

    JSONObject getParams() {
        return params;
    }

    public boolean isInvalid() {
        return !badFields.isEmpty();
    }
}
