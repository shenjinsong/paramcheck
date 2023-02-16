package org.warai.paramcheck.config;

import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author 大叔
 * @Time: 2020/4/17 16:38
 */
public class RequestReaderHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] inputStreamBytes;
    private Map parameterMap;

    public RequestReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.parameterMap = request.getParameterMap();
        inputStreamBytes = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Map getParameterMap() {
        return this.parameterMap;
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStreamBytes);
        return new ServletInputStream() {
            @Override
            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

}