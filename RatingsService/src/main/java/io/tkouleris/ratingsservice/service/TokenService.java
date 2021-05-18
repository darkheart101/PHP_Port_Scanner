package io.tkouleris.ratingsservice.service;

import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private static TokenService instance = null;
    private String token;

    private TokenService(){}

    public static TokenService getInstance(){
        if(TokenService.instance == null){
            TokenService.instance = new TokenService();
        }
        return TokenService.instance;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
