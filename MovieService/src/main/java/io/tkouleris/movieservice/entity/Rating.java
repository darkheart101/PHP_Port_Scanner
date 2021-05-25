package io.tkouleris.movieservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rating {
    @JsonProperty("id")
    public long Id;

    @JsonProperty("movie_id")
    public Long movie_id;

    @JsonProperty("rate")
    public double rate;

    @JsonProperty("userId")
    public long userId;

    public long getUserId() {
        return userId;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public Long getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(Long movie_id) {
        this.movie_id = movie_id;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String toString()
    {
        return "{ \"movie_id\":" + getMovie_id() +", \"rate\": "+getRate()+",\"userId\":"+getUserId()+", \"id\": "+getId()+"}";
    }
}
