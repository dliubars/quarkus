package io.quarkus.it.spring.data.jpa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

@Path("/movie")
public class MovieResource {

    private final MovieRepository movieRepository;

    public MovieResource(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    public Iterable<Movie> all() {
        return movieRepository.findAll();
    }

    @GET
    @Path("/first/orderByDuration")
    @Produces("application/json")
    public Movie findFirstByOrOrderByTitleDesc() {
        return movieRepository.findFirstByOrderByDurationDesc();
    }

    @GET
    @Path("/title/{title}")
    @Produces("application/json")
    public Movie findByTitle(@PathParam("title") String title) {
        return movieRepository.findByTitle(title);
    }

    @GET
    @Path("/title/titleLengthOrder/page/{size}/{num}")
    public String orderByTitleLengthSlice(@PathParam("size") int pageSize, @PathParam("num") int pageNum) {
        Slice<Movie> slice = movieRepository.orderByTitleLength(PageRequest.of(pageNum, pageSize));
        return slice.hasNext() + " / " + slice.getNumberOfElements();
    }

    @GET
    @Path("/rating/{rating}")
    @Produces("application/json")
    public List<Movie> findByRating(@PathParam("rating") String rating) {
        Iterator<Movie> byRating = movieRepository.findByRating(rating);
        List<Movie> result = new ArrayList<>();
        byRating.forEachRemaining(result::add);
        return result;
    }

    @GET
    @Path("/rating/{rating}/durationLargerThan/{duration}")
    @Produces("application/json")
    public List<Movie> withTitleAndDurationLargerThan(@PathParam("rating") String rating, @PathParam("duration") int duration) {
        return movieRepository.withRatingAndDurationLargerThan(duration, rating);
    }

    @GET
    @Path("/title/like/{title}")
    @Produces("application/json")
    public List<Object[]> someFieldsWithTitleLike(@PathParam("title") String title) {
        return movieRepository.someFieldsWithTitleLike(title, Sort.by(new Sort.Order(Sort.Direction.ASC, "duration")));
    }

    @GET
    @Path("/delete/rating/{rating}")
    public void deleteByRating(@PathParam("rating") String rating) {
        movieRepository.deleteByRating(rating);
    }

    @GET
    @Path("/delete/title/{title}")
    public Long deleteByTitleLike(@PathParam("title") String title) {
        return movieRepository.deleteByTitleLike(title);
    }

    @GET
    @Path("/change/rating/{rating}/{newRating}")
    public Integer changeRatingToNewName(@PathParam("rating") String rating, @PathParam("newRating") String newRating) {
        return movieRepository.changeRatingToNewName(newRating, rating);
    }

    @GET
    @Path("/nullify/rating/forTitle/{title}")
    public void setRatingToNullForTitle(@PathParam("title") String title) {
        movieRepository.setRatingToNullForTitle(title);
    }
}
