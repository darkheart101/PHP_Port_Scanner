package io.tkouleris.ratingsservice.repository;

import io.tkouleris.ratingsservice.entity.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IRatingRepository extends CrudRepository<Rating, Long> {
    @Query("select r from Rating r where r.userId =?1")
    List<Rating> findRatingByUser(long userId);

    @Query("select r from Rating r where r.userId=?1 and r.movie_id=?2")
    Optional<Rating> findRatingByUserAndMovie(long userId, long movieId);
}
