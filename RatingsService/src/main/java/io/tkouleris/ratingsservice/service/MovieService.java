package io.tkouleris.ratingsservice.service;

import io.tkouleris.ratingsservice.dto.otherResponse.MovieResponse;
import io.tkouleris.ratingsservice.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieService {

    @Autowired
    private RestTemplate restTemplate;

    public MovieResponse getMovie(long movieId){
        String token = getToken();
        var entity = this.setHeaders(token);
        ResponseEntity<MovieResponse> response = restTemplate.exchange(
                "http://movies-service/movies/" + movieId,
                HttpMethod.GET,
                entity,
                MovieResponse.class
        );
        return response.getBody();
    }

    private String getToken() {
        TokenService tokenService = TokenService.getInstance();
        return tokenService.getToken();
    }


    private HttpEntity setHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(headers);
    }
}
