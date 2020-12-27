package org.warai.paramcheck;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.warai.paramcheck.annotation.ParamCheck;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: わらい
 * @Time: 2020/9/22 16:26
 */
public class ErrorResultHandler {

   public void handler(JSONObject params, List<String> badFields, ParamCheck paramCheck) throws IOException {
        Map<String, Object> map = new HashMap<>(1);
        map.put("code", paramCheck.errorCode());
        map.put("msg", paramCheck.msg());
        this.handler(map, paramCheck.httpCode());
    }

    public void handler(Map responseMsg, int status) throws IOException {
        new ParamException(responseMsg,  status).build();
    }

    private class ParamException extends Exception{

        private Map msg;
        private int status;

        ParamException(Map msg, int status){
            this.msg = msg;
            this.status = status;
        }

        void build() throws IOException {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null){
                HttpServletResponse response = requestAttributes.getResponse();
                if (response != null){
                    response.setStatus(status);
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    response.setHeader("Content-type", "text/html;charset=UTF-8");
//                    response.setCharacterEncoding(StandardCharsets.ISO_8859_1.name());
                    PrintWriter out = response.getWriter();
                    String json = JSON.toJSONString(msg);
                    out.write(json);
                    out.flush();
                    out.close();
                }
            }

        }
    }
}
