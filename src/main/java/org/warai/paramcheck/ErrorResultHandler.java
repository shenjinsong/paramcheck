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
import java.util.Map;
import java.util.Set;

/**
 * @Auther: わらい
 * @Time: 2020/9/22 16:26
 */
public interface ErrorResultHandler {

    void handler(JSONObject params, Map<String, Set<String>> badFields, ParamCheck paramCheck) throws IOException;


    default void handler(Map responseMsg, int status) throws IOException {
        new ResponseResult(responseMsg,  status).build();
    }

     class ResponseResult{

        private Map msg;
        private int status;

        ResponseResult(Map msg, int status){
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
