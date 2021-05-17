package io.tkouleris.movieservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class TokenService {

    private static TokenService instance = null;
    private String token;


    private TokenService(){

    }

    public static TokenService getInstance(){
        if(instance == null){
            return new TokenService();
        }
        return instance;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
