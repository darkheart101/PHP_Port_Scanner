package io.tkouleris.movieservice.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.dto.response.ApiResponse;
import io.tkouleris.movieservice.dto.response.RatedMovie;
import io.tkouleris.movieservice.entity.Movie;
import io.tkouleris.movieservice.entity.Rating;
import io.tkouleris.movieservice.entity.User;
import io.tkouleris.movieservice.repository.IMovieRepository;
import io.tkouleris.movieservice.service.Authentication;
import io.tkouleris.movieservice.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private RatingService ratingService;

    @GetMapping(path="/all", produces = "application/json")
    public ResponseEntity<Object> getAll(){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");

        RatingsResponse ratings = ratingService.getAll();
        System.out.println("33333333333333333333333333333333333333333333333");
        if(ratings == null){
            System.out.println("NULL");
        }


//        RatingsResponse ratings = restTemplate.getForObject("http://ratings-service/ratings/all",RatingsResponse.class);

        List<Movie> movies = (List<Movie>) movieRepository.findAll();

        List<RatedMovie> ratedMovies = new ArrayList<>();
        for (Movie movie : movies) {
            for (Rating rating : ratings.data) {
                if(rating.getMovie_id() == movie.getId()){
                    RatedMovie ratedMovie = new RatedMovie();
                    ratedMovie.id = movie.getId();
                    ratedMovie.title = movie.getTitle();
                    ratedMovie.rate = rating.getRate();
                    ratedMovies.add(ratedMovie);
                }
            }
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(ratedMovies);
        apiResponse.setMessage("Movies");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

//    public ResponseEntity<Object> getFallbackAllMovies(){
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setMessage("No movies");
//        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
//    }
}


