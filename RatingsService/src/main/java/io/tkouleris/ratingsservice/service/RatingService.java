package io.tkouleris.ratingsservice.service;

import io.tkouleris.ratingsservice.dto.request.RatingDto;
import io.tkouleris.ratingsservice.entity.Rating;
import io.tkouleris.ratingsservice.entity.User;
import io.tkouleris.ratingsservice.repository.IRatingRepository;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private final IRatingRepository ratingRepository;

    public RatingService(IRatingRepository ratingRepository){
        this.ratingRepository = ratingRepository;
    }

    public Rating createOrUpdate(User user, RatingDto ratingDto){
        //TODO check if movie exists

        Rating rating = this.ratingRepository.findRatingByUserAndMovie(user.getId(), ratingDto.movieId).orElse(null);
        if(rating == null){
            rating = new Rating();
        }
        rating.setRate(ratingDto.rate);
        rating.setMovie_id(ratingDto.movieId);
        rating.setUserId(user.getId());
        return this.ratingRepository.save(rating);
    }
}
