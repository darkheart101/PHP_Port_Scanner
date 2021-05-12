package io.tkouleris.movieservice.interceptor;

import io.tkouleris.movieservice.service.Authentication;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor extends HandlerInterceptorAdapter {

    Authentication authentication = new Authentication();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!authentication.verify()){
            response.sendError(401,"Unauthorized");
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
