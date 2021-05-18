package io.tkouleris.movieservice.service;

import io.tkouleris.movieservice.dto.otherResponse.AuthResponse;
import io.tkouleris.movieservice.entity.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class Authentication {

    private User loggedInUser;

    public boolean verify() {
        String token = this.getToken();
        var entity = this.setHeaders(token);
        RestTemplate authRestTemplate = new RestTemplate();
        try {
            ResponseEntity<AuthResponse> response = authRestTemplate.exchange("http://127.0.0.1:8000/api/verify", HttpMethod.POST, entity, AuthResponse.class);
            if (response.getBody() != null) {
                this.loggedInUser = response.getBody().user;
                return true;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    public String getToken() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");
    }

    private HttpEntity setHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(headers);
    }

    public User getLoggedInUser() {
        return this.loggedInUser;
    }
}