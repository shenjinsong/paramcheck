package com.warai.paramcheck;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.warai.paramcheck.annotation.ParamCheck;
import com.warai.paramcheck.handler.ErrorResultHandler;
import com.warai.paramcheck.handler.FieldInspect;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.management.BadStringOperationException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * 使用拦截器判断参数
 *
 * @author わらい
 * @date 2020/04/20
 */
public class ParamCheckIntercept extends HandlerInterceptorAdapter {

    private static Logger log = Logger.getLogger(ParamCheckIntercept.class.getName());
    private static ErrorResultHandler errorResultHandler;


    static {

        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(""));
        Set<Class<? extends ErrorResultHandler>> classes = reflections.getSubTypesOf(ErrorResultHandler.class);

        if (!ObjectUtils.isEmpty(classes)) {
            if (classes.size() > 1) {
                throw new BeanInitializationException("Multiple duplicates of beans 'errorResultHandler', There can only be one subclass");
            }
            Class<? extends ErrorResultHandler> aClass = classes.iterator().next();
            try {
                errorResultHandler = aClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

        } else {
            errorResultHandler = new ErrorResultHandler();
        }

        String name = errorResultHandler.getClass().getName();
        log.info("*** Initialize the " + name + " processor ***");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 判断是有@PramCheck 注解和注解中是否存在需要校验的字段， 没有直接返回
        ParamCheck paramCheck = method.getAnnotation(ParamCheck.class);
        if (paramCheck == null || ObjectUtils.isEmpty(paramCheck.value())) {
            return true;
        }

        Annotation[][] parameterAnnotationArrays = method.getParameterAnnotations();

        // 判断请求参数是否是请求体传过来
        boolean isRequestBody = false;
        query:
        for (Annotation[] parameterAnnotation : parameterAnnotationArrays) {
            for (Annotation annotation : parameterAnnotation) {
                Class<? extends Annotation> aClass = annotation.annotationType();
                isRequestBody = RequestBody.class.equals(aClass);
                if (isRequestBody) {
                    break query;
                }
            }
        }

        // 拿到可重用的流
        ServletRequest servletRequest = new RequestReaderHttpServletRequestWrapper(request);

        // 检查参数
        FieldInspect fieldInspect = this.checkReqParams(paramCheck, servletRequest, isRequestBody);
        if (fieldInspect.isInvalid()) {
            errorResultHandler.handler(fieldInspect.getBadFields(), paramCheck);
            return false;
        }
        return true;
    }


    private FieldInspect checkReqParams(ParamCheck paramCheck, ServletRequest request, boolean isRequestBody) throws BadStringOperationException {

        JSONObject jsonObject;

        // 链接中包含参数，和请求体中参数校验过程
        if (isRequestBody) {
            jsonObject = this.parseReqBodyParams(request);
        } else {
            String jsonStr = JSON.toJSONString(request.getParameterMap());
            jsonObject = JSON.parseObject(jsonStr);
        }
        FieldInspect fieldCheck = new FieldInspect(paramCheck, jsonObject);
        fieldCheck.checkParam();
        return fieldCheck;
    }


    private JSONObject parseReqBodyParams(ServletRequest servletRequest) {


        try (InputStreamReader inputStreamReader = new InputStreamReader(servletRequest.getInputStream(), StandardCharsets.UTF_8)) {

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // 将请求中的请求体通过流读成字符
            String jsonStr = bufferedReader.lines().collect(Collectors.joining());
            // 传入的json数据序列化为Json对象
            return JSONObject.parseObject(jsonStr);

        } catch (Exception ignore) {
            return null;
        }
    }


}
