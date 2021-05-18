package io.tkouleris.movieservice.interceptor;

import io.tkouleris.movieservice.service.Authentication;
import io.tkouleris.movieservice.service.LoggedUserService;
import io.tkouleris.movieservice.service.TokenService;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor extends HandlerInterceptorAdapter {

    Authentication authentication = new Authentication();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TokenService tokenService = TokenService.getInstance();
        tokenService.setToken(authentication.getToken());

        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        loggedUserService.setLoggedInUser(authentication.getLoggedInUser());

        if(!authentication.verify()){
            response.sendError(401,"Unauthorized");
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
