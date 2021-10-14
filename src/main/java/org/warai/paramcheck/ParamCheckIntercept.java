package org.warai.paramcheck;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.warai.paramcheck.annotation.ParamCheck;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
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

    @Component
    public static class SpringUtil implements ApplicationContextAware {
        private static ApplicationContext applicationContext = null;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            if (SpringUtil.applicationContext == null) {
                SpringUtil.applicationContext = applicationContext;
            }
        }

        public static <T> T getBean(Class<T> clazz) {
            return applicationContext.getBean(clazz);
        }
    }

    static {
        try {
            errorResultHandler = SpringUtil.getBean(ErrorResultHandler.class);
            log.info("*** Initialize the ErrorResultHandler processor ***");
        } catch (Exception e) {
            throw new NoSuchBeanDefinitionException("ParamCheck lacks the error result handling class and needs to inherit the ErrorResultHandler interface");
        }
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
        if (paramCheck == null) {
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

        // 获取参数
        JSONObject jsonObject;
        // 链接中包含参数，和请求体中参数校验过程
        if (isRequestBody) {
            jsonObject = this.parseReqBodyParams(request);
        } else {
            String jsonStr = JSON.toJSONString(request.getParameterMap());
            jsonObject = JSON.parseObject(jsonStr);
        }

        long l2 = System.currentTimeMillis();
        FieldInspect fieldInspect = new FieldInspect(paramCheck, jsonObject).checkParam();
        long l3 = System.currentTimeMillis();
        log.warning("*** 参数校验耗时：" + (l3 - l2) + " ms ***");
        if (fieldInspect.isInvalid()) {
            errorResultHandler.handler(fieldInspect.getParams(), fieldInspect.getBadFields(), paramCheck);
            return false;
        }
        return true;
    }

    private JSONObject parseReqBodyParams(ServletRequest servletRequest) {


        try (InputStreamReader inputStreamReader = new InputStreamReader(servletRequest.getInputStream(), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            // 将请求中的请求体通过流读成字符
            String jsonStr = bufferedReader.lines().collect(Collectors.joining());
            // 传入的json数据序列化为Json对象
            return JSONObject.parseObject(jsonStr);

        } catch (Exception ignore) {
            return null;
        }
    }


}
