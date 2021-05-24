package io.tkouleris.movieservice.controller;

import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.dto.response.ApiResponse;
import io.tkouleris.movieservice.dto.response.RatedMovie;
import io.tkouleris.movieservice.entity.Movie;
import io.tkouleris.movieservice.entity.Rating;
import io.tkouleris.movieservice.repository.IMovieRepository;
import io.tkouleris.movieservice.service.MovieService;
import io.tkouleris.movieservice.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/movies")
public class MoviesController {

    @Autowired
    private IMovieRepository movieRepository;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private MovieService movieService;

    @GetMapping(path="/rated", produces = "application/json")
    public ResponseEntity<Object> getRatedMovies() throws IOException {
        RatingsResponse ratings = ratingService.getAll();

        if(ratings.data == null){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Movies");
            return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
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

    @GetMapping(path="/unrated", produces = "application/json")
    public ResponseEntity<Object> getUnratedMovies() throws IOException {
        List<Movie> movies = (List<Movie>) movieRepository.findAll();
        RatingsResponse ratings = ratingService.getAll();

        if(ratings.data == null){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setData(movies);
            apiResponse.setMessage("Unrated movies");
            return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
        }

        List<RatedMovie> ratedMovies = new ArrayList<>();
        List<Movie> excludedMovies = new ArrayList<>();
        for (Rating rating : ratings.data) {
            for (Movie movie : movies) {
                if(rating.getMovie_id() == movie.getId()){
                    excludedMovies.add(movie);
                }
            }
        }


        for(Movie movie : movies){
            boolean isRated = false;
            for(Movie excludedMovie: excludedMovies){
                if(movie.getId() == excludedMovie.getId()){
                    isRated = true;
                }
            }
            if(!isRated){
                RatedMovie ratedMovie = new RatedMovie();
                ratedMovie.id = movie.getId();
                ratedMovie.title = movie.getTitle();
                ratedMovies.add(ratedMovie);
            }
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(ratedMovies);
        apiResponse.setMessage("Unrated movies");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

    @GetMapping(path="/{movieId}", produces = "application/json")
    public ResponseEntity<Object> getMovie(@PathVariable("movieId") long movieId){

        Movie movie = this.movieService.getMovie(movieId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(movie);
        apiResponse.setMessage("Movie");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }
}


