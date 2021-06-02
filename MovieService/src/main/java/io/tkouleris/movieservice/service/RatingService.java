package io.tkouleris.movieservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.entity.User;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;


@Service
public class RatingService {

    private final RestTemplate restTemplate;

    private final CacheService cacheService;

    private final RatingFallbackService ratingFallbackService;

    public RatingService(RestTemplate restTemplate, CacheService cacheService, RatingFallbackService ratingFallbackService) {
        this.restTemplate = restTemplate;
        this.cacheService = cacheService;
        this.ratingFallbackService = ratingFallbackService;
    }


//    @HystrixCommand(fallbackMethod = "getFallbackAllMovies")
    public RatingsResponse getAll() throws IOException {
        String token = getToken();
        var entity = this.setHeaders(token);
        ResponseEntity<RatingsResponse> response = restTemplate.exchange(
                "http://ratings-service/ratings/all",
                HttpMethod.GET,
                entity,
                RatingsResponse.class
        );
        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        User loggedInUser = loggedUserService.getLoggedInUser();
        this.cacheService.save(Objects.requireNonNull(response.getBody()).toString(), loggedInUser.getId() + "_all_ratings");

        return response.getBody();
    }

    @HystrixCommand(fallbackMethod = "getFallbackMovie")
    public RatingsResponse getMovieRating(long movie_id) throws IOException {
        String token = getToken();
        var entity = this.setHeaders(token);
        ResponseEntity<RatingsResponse> response = restTemplate.exchange(
                "http://ratings-service/ratings/movie/" + movie_id,
                HttpMethod.GET,
                entity,
                RatingsResponse.class
        );
        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        User loggedInUser = loggedUserService.getLoggedInUser();
        this.cacheService.save(Objects.requireNonNull(response.getBody()).toString(), loggedInUser.getId() + "_" + movie_id + "_movie_rating");

        return response.getBody();
    }

    public RatingsResponse getFallbackAllMovies() throws FileNotFoundException, JsonProcessingException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        return this.ratingFallbackService.getFallbackAllMovies();
    }

    public RatingsResponse getFallbackMovie(long movie_id) throws FileNotFoundException, JsonProcessingException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        return this.ratingFallbackService.getFallbackMovie(movie_id);
    }

    private HttpEntity setHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(headers);
    }


    private String getToken() {
        TokenService tokenService = TokenService.getInstance();
        return tokenService.getToken();
    }
}
