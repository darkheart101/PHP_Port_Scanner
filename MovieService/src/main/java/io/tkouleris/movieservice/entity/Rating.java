package io.tkouleris.movieservice.entity;

public class Rating {

    private long Id;

    private Long movie_id;

    private double rate;

    private long userId;

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
