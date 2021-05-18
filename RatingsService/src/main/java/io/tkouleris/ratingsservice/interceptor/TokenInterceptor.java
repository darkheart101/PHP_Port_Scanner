package io.tkouleris.ratingsservice.interceptor;


import io.tkouleris.ratingsservice.service.Authentication;
import io.tkouleris.ratingsservice.service.LoggedUserService;
import io.tkouleris.ratingsservice.service.TokenService;
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
