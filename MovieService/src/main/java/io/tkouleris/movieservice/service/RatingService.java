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

    /**
     * Gets all the ratings from the rating service, caches it data and returns the data
     * @return A RatingResponse object with the data
     * @throws IOException When cannot write on the cache file
     */
    @HystrixCommand(fallbackMethod = "getFallbackAllMovies")
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

    /**
     * Gets back the rating of the movie.
     * @param movie_id the movie id that the user wants the rating
     * @return Movie object with the details
     * @throws IOException When cannot save the cache file
     */
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

    /**
     * Hystrix fallback function when getAll fails
     * @return The cached data
     * @throws FileNotFoundException when cache file not found
     * @throws JsonProcessingException when cached data cannot be read
     */
    public RatingsResponse getFallbackAllMovies() throws FileNotFoundException, JsonProcessingException{
        return this.ratingFallbackService.getFallbackAllMovies();
    }

    /**
     * Hystrix fallback function when getMovieRating fails
     * @param movie_id the id of the movie you need data
     * @return cached data
     * @throws FileNotFoundException when cannot read the cache file
     * @throws JsonProcessingException when cached data cannot be read
     */
    public RatingsResponse getFallbackMovie(long movie_id) throws FileNotFoundException, JsonProcessingException{
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
