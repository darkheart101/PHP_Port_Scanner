package io.tkouleris.movieservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.tkouleris.movieservice.dto.otherResponse.AuthResponse;
import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.dto.response.ApiResponse;
import io.tkouleris.movieservice.entity.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Authentication authentication;

    @HystrixCommand(fallbackMethod = "getFallbackAllMovies")
    public RatingsResponse getAll()
    {
        String token = getToken();
        var entity = this.setHeaders(token);
        ResponseEntity<RatingsResponse> response = restTemplate.exchange(
                "http://ratings-service/ratings/all",
                HttpMethod.GET,
                entity,
                RatingsResponse.class
        );
        return response.getBody();
    }

    private String getToken(){
        TokenService tokenService = TokenService.getInstance();
        return tokenService.getToken();
    }

    public RatingsResponse getFallbackAllMovies(){
        RatingsResponse response = new RatingsResponse();
        Rating r = new Rating();
        r.setMovie_id(1L);
        r.setId(1);
        r.setRate(10.0);
        List<Rating> arr = new ArrayList<>();
        arr.add(r);
        response.data = arr;
        response.timestamp = "xxx";

        return response;
    }

    private HttpEntity setHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        System.out.println(token);
        headers.set("Authorization",token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(headers);
    }


}
