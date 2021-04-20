package io.tkouleris.movieservice.repository;

import io.tkouleris.movieservice.entity.Movie;
import org.springframework.data.repository.CrudRepository;

public interface IMovieRepository extends CrudRepository<Movie, Long> {
}
