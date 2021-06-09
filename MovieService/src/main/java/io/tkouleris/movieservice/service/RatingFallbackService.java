package io.tkouleris.movieservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.entity.Rating;
import io.tkouleris.movieservice.entity.User;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class RatingFallbackService {

    private final CacheService cacheService;

    public RatingFallbackService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public RatingsResponse getFallbackMovie(long movie_id) throws FileNotFoundException, JsonProcessingException {
        System.out.println("========================= FALLBACK MOVIE ================================");
        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        User user = loggedUserService.getLoggedInUser();
        Rating[] ratings = new Rating[0];
        ratings = this.cacheService.getDataOfKey(user.getId() +"_"+ movie_id +"_movie_rating", ratings, Rating[].class);

        RatingsResponse ratingsResponse = new RatingsResponse();
        ratingsResponse.data = new ArrayList<>();
        ratingsResponse.data.addAll(Arrays.asList(ratings));
        ratingsResponse.message = "Ratings";
        ratingsResponse.timestamp = LocalDateTime.now().toString();
        return ratingsResponse;
    }

    public RatingsResponse getFallbackAllMovies() throws FileNotFoundException, JsonProcessingException{
        System.out.println("========================= FALLBACK ================================");
        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        User user = loggedUserService.getLoggedInUser();
        Rating[] ratings = new Rating[0];
        ratings = this.cacheService.getDataOfKey(user.getId() + "_all_ratings", ratings, Rating[].class);
        RatingsResponse ratingsResponse = new RatingsResponse();
        ratingsResponse.data = new ArrayList<>();
        ratingsResponse.data.addAll(Arrays.asList(ratings));
        ratingsResponse.message = "Ratings";
        ratingsResponse.timestamp = LocalDateTime.now().toString();
        return ratingsResponse;
    }
}
