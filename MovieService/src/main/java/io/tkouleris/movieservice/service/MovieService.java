package io.tkouleris.movieservice.service;

import io.tkouleris.movieservice.dto.otherResponse.RatingsResponse;
import io.tkouleris.movieservice.entity.Movie;
import io.tkouleris.movieservice.entity.Rating;
import io.tkouleris.movieservice.repository.IMovieRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    private final IMovieRepository movieRepository;
    private final RatingService ratingService;


    public MovieService(IMovieRepository movieRepository, RatingService ratingService){
        this.movieRepository = movieRepository;
        this.ratingService = ratingService;
    }

    public Movie getMovie(long id){
        return this.movieRepository.findById(id).orElse(null);
    }


    /**
     * This method adds a movie in the database when the movie does not exist
     * @param movie The movie that you want to add
     * @return The newly inserted movie
     */
    public Movie addMovie(Movie movie){
        Movie db_movie = movieRepository.findMovieByTitle(movie.getTitle()).orElse(null);
        if(db_movie != null){
            return null;
        }

        return movieRepository.save(movie);
    }


    /**
     * Gets all the movies that the user not rated yet
     * @return List<Movie>
     */
    public List<Movie> getUnratedMovies() throws IOException {
        List<Movie> unratedMovies = new ArrayList<>();

        List<Movie> movies = (List<Movie>) movieRepository.findAll();
        RatingsResponse ratings = this.ratingService.getAll();

        if(ratings.data == null){
            return unratedMovies;
        }

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
                    break;
                }
            }
            if(!isRated){
                unratedMovies.add(movie);
            }
        }

        return unratedMovies;
    }
}
