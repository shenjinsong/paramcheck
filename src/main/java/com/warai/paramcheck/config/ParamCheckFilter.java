package com.warai.paramcheck.config;


import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
public class ParamCheckFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig){}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        ServletRequest requestWrapper = null;
        if (request instanceof HttpServletRequest) {
            requestWrapper = new RequestReaderHttpServletRequestWrapper((HttpServletRequest) request);
        }

        if (requestWrapper == null) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(requestWrapper, response);
        }
    }


    @Override
    public void destroy() {

    }
}