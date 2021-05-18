package io.tkouleris.ratingsservice.repository;

import io.tkouleris.ratingsservice.entity.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRatingRepository extends CrudRepository<Rating, Long> {
    @Query("select r from Rating r where r.userId =?1")
    List<Rating> findRatingByUser(long user_id);
}
