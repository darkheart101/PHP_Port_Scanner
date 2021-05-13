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
        System.out.println("RATINGS");
        String token = authentication.getToken();
        var entity = this.setHeaders(token);
        ResponseEntity<RatingsResponse> response = restTemplate.exchange(
                "http://ratings-service/ratings/all",
                HttpMethod.GET,
                entity,
                RatingsResponse.class
        );
        System.out.println(response.getBody());
        return response.getBody();
    }

    public RatingsResponse getFallbackAllMovies(){
        System.out.println("FALLBACK");
        RatingsResponse response = new RatingsResponse();
        System.out.println("FALLBACK_______1");
        Rating r = new Rating();
        r.setMovie_id(1L);
        System.out.println("FALLBACK_______2");
        r.setId(1);
        System.out.println("FALLBACK_______3");
        r.setRate(10.0);
        System.out.println("FALLBACK_______4");
        List<Rating> arr = new ArrayList<>();
        arr.add(r);
        response.data = arr;
        System.out.println("FALLBACK_______5");
        response.message = "fallback";
        System.out.println("FALLBACK_______6");
        response.timestamp = "xxx";
        System.out.println("FALLBACK_______7");


        return response;
    }

    private HttpEntity setHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(headers);
    }


}
