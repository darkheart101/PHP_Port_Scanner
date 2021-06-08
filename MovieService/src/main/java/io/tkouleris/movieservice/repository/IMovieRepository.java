package io.tkouleris.movieservice.repository;

import io.tkouleris.movieservice.entity.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IMovieRepository extends CrudRepository<Movie, Long> {
    Optional<Movie> findMovieByTitle(String title);
}
