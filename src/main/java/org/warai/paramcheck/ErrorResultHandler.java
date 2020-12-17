package org.warai.paramcheck;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.warai.paramcheck.annotation.ParamCheck;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
        this.handler(map, HttpStatus.PRECONDITION_FAILED);
    }

    public void handler(Map responseMsg, HttpStatus status) throws IOException {
        new ParamException(responseMsg,  status).build();
    }

    private class ParamException extends Exception{

        private Map msg;
        private HttpStatus status;

        ParamException(Map msg, HttpStatus status){
            this.msg = msg;
            this.status = status;
        }

        void build() throws IOException {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null){
                HttpServletResponse response = requestAttributes.getResponse();
                if (response != null){
                    response.setStatus(status.value());
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
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
