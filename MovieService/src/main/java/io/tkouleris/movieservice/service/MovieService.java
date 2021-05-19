package io.tkouleris.movieservice.service;

import io.tkouleris.movieservice.entity.Movie;
import io.tkouleris.movieservice.repository.IMovieRepository;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private IMovieRepository movieRepository;

    public MovieService(IMovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public Movie getMovie(long id){
        return this.movieRepository.findById(id).orElse(null);
    }
}
