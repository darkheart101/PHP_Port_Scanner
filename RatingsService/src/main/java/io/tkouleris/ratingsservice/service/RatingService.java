package io.tkouleris.ratingsservice.service;

import io.tkouleris.ratingsservice.dto.otherResponse.MovieResponse;
import io.tkouleris.ratingsservice.dto.request.RatingDto;
import io.tkouleris.ratingsservice.entity.Rating;
import io.tkouleris.ratingsservice.entity.User;
import io.tkouleris.ratingsservice.exception.NotFoundException;
import io.tkouleris.ratingsservice.repository.IRatingRepository;
import org.springframework.stereotype.Service;

@Service
public class RatingService {

    private final IRatingRepository ratingRepository;
    private final MovieService movieService;

    public RatingService(IRatingRepository ratingRepository, MovieService movieService){
        this.ratingRepository = ratingRepository;
        this.movieService = movieService;
    }

    public Rating createOrUpdate(User user, RatingDto ratingDto) throws NotFoundException {

        MovieResponse movieResponse = this.movieService.getMovie(ratingDto.movieId);
        if(movieResponse.data == null){
            throw new NotFoundException("Movie not found");
        }
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
