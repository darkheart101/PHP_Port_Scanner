package io.tkouleris.movieservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.tkouleris.movieservice.dto.otherResponse.AuthResponse;
import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.dto.response.ApiResponse;
import io.tkouleris.movieservice.entity.Rating;
import io.tkouleris.movieservice.entity.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;


@Service
public class RatingService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheService cacheService;



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
        // cache
        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        User user = loggedUserService.getLoggedInUser();
        this.cacheService.save(response.getBody().toString(),String.valueOf(user.getId())+"_all_ratings");

        return response.getBody();
    }

    @HystrixCommand(fallbackMethod = "getFallbackMovie")
    public RatingsResponse getMovieRating(long movie_id) throws IOException {
        String token = getToken();
        var entity = this.setHeaders(token);
        ResponseEntity<RatingsResponse> response = restTemplate.exchange(
                "http://ratings-service/ratings/movie/"+movie_id,
                HttpMethod.GET,
                entity,
                RatingsResponse.class
        );

        // cache
        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        User user = loggedUserService.getLoggedInUser();
        this.cacheService.save(response.getBody().toString(),String.valueOf(user.getId())+"_"+String.valueOf(movie_id)+"_movie_rating");

        return response.getBody();
    }

    private String getToken() {
        TokenService tokenService = TokenService.getInstance();
        return tokenService.getToken();
    }

    public RatingsResponse getFallbackAllMovies() throws FileNotFoundException, JsonProcessingException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.out.println("========================= FALLBACK ================================");
        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        User user = loggedUserService.getLoggedInUser();
        Rating[] ratings = new Rating[0];
        ratings = (Rating[]) this.cacheService.getKey(String.valueOf(user.getId())+"_all_ratings", ratings, Rating[].class);
        RatingsResponse ratingsResponse = new RatingsResponse();
        ratingsResponse.data = new ArrayList<>();
        ratingsResponse.data.addAll(Arrays.asList(ratings));
        ratingsResponse.message = "Ratings";
        ratingsResponse.timestamp = LocalDateTime.now().toString();
        return ratingsResponse;

    }

    public RatingsResponse getFallbackMovie(long movie_id) throws FileNotFoundException, JsonProcessingException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        System.out.println("========================= FALLBACK MOVIE ================================");
        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        User user = loggedUserService.getLoggedInUser();
        Rating rating = new Rating();
        rating = (Rating) this.cacheService.getKey(String.valueOf(user.getId())+"_"+String.valueOf(movie_id)+"_movie_rating", rating, Rating.class);
        System.out.println(rating);
        RatingsResponse ratingsResponse = new RatingsResponse();
        ratingsResponse.data = new ArrayList<>();
        ratingsResponse.data.addAll(Arrays.asList(rating));
        ratingsResponse.message = "Ratings";
        ratingsResponse.timestamp = LocalDateTime.now().toString();
        return ratingsResponse;
    }

    private HttpEntity setHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity(headers);
    }


}
