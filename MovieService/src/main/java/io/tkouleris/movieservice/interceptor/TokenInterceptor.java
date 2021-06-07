package io.tkouleris.movieservice.interceptor;

import io.tkouleris.movieservice.service.Authentication;
import io.tkouleris.movieservice.service.LoggedUserService;
import io.tkouleris.movieservice.service.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    Authentication authentication;

    public TokenInterceptor(Authentication authentication) {
        this.authentication = authentication;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!authentication.verify()){
            response.sendError(401,"Unauthorized");
            return false;
        }
        TokenService tokenService = TokenService.getInstance();
        tokenService.setToken(authentication.getToken());

        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        loggedUserService.setLoggedInUser(authentication.getLoggedInUser());

        return super.preHandle(request, response, handler);
    }
}
