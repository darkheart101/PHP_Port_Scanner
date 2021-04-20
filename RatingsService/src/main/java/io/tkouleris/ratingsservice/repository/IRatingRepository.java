package io.tkouleris.ratingsservice.repository;

import io.tkouleris.ratingsservice.entity.Rating;
import org.springframework.data.repository.CrudRepository;

public interface IRatingRepository extends CrudRepository<Rating, Long> {
}
