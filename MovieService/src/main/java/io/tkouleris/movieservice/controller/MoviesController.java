package io.tkouleris.movieservice.controller;

import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.dto.response.ApiResponse;
import io.tkouleris.movieservice.dto.response.RatedMovie;
import io.tkouleris.movieservice.entity.Movie;
import io.tkouleris.movieservice.entity.Rating;
import io.tkouleris.movieservice.repository.IMovieRepository;
import io.tkouleris.movieservice.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private RatingService ratingService;

    @GetMapping(path="/all", produces = "application/json")
    public ResponseEntity<Object> getAll(){
        RatingsResponse ratings = ratingService.getAll();
        if(ratings == null){
            System.out.println("NULL");
        }

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


