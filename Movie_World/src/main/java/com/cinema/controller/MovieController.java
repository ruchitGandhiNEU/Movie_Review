/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.controller;


import com.cinema.Exceptions.UserGeneratedExceptions;
import com.cinema.model.Movie;
import com.cinema.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */

@RestController
@RequestMapping("/api")
public class MovieController {
    
    
    @Autowired
    @Qualifier("movieService")
    MovieService movieService;
    
    
    //This method will help us get all the movies in the data base so that we can diplay.
    @RequestMapping(value = "/movies/all",
            method = RequestMethod.GET,
            produces = "application/json")
    public  ResponseEntity<Object> getAllMovies() throws UserGeneratedExceptions{
            return movieService.getAllMovies();
    }
    
    
    
     // If Admin wants to delete a Movie, He can delete it and all its mapping will be deleted.
    //All validations are done in service class
    @RequestMapping(value = "/movies/delete",
            method = RequestMethod.POST,
            produces = "application/json")
    public  ResponseEntity<Object> deleteAMovie(@RequestBody Movie movie,HttpServletRequest request) throws UserGeneratedExceptions{
        return movieService.deleteAMovie(movie, request);        
    }
    
    
    //This method will help Admin add a new movie to the data base once he decides.
        @RequestMapping(value = "/movies/add",
            method = RequestMethod.POST,
            produces = "application/json")
    public  ResponseEntity<Object> addNewMovie(@RequestBody Movie movie,HttpServletRequest request) throws UserGeneratedExceptions{
       return movieService.addNewMovie(movie, request);
        
        
    }
    
    
    // If a user wants to add a movie to his database he may use this api to add. Post body would contain the movie object.
    //All validations for user/movie existance are taken care of in service class.
    @RequestMapping(value = "/movies/watchlist/add",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> addToWatchList(@RequestBody Movie movie, HttpServletRequest request) throws Exception {
        return movieService.addToWatchList(movie, request);
    }

        // If a user wants to remove a movie to his database he may use this api to add. Post body would contain the movie object.
    //All validations for user/movie existance are taken care of in service class.
    //Mappings are also validated.
    @RequestMapping(value = "/movies/watchlist/remove",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> removeFromWatchList(@RequestBody Movie movie, HttpServletRequest request) throws Exception {
        return movieService.removeFromWatchList(movie, request);
    }

    
    
    //If we want to check if a movie is in user's watchlist we can check that from here.
    // It will be usefull when we want to help user add or delete movie from watch list.
    @RequestMapping(value = "/movies/watchlist",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> isWatchList(@RequestBody Movie movie, HttpServletRequest request) throws Exception {
        return movieService.isWatchList(movie, request);
    }

    //If we want to get all the list of movies in user's watchlist we can get it from this method.
    //User is validated to check that he can only call for his list.
    @RequestMapping(value = "/movies/watchlist/{userId}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> getWatchList(@PathVariable String userId) {
        return movieService.getWatchList(userId);
    }

    
    //Add a rating to the movie for a user.
    //User is validated from the token.
    // Rating validation done in service for 0 < x > 10.
    @RequestMapping(value = "/movies/ratings/add/{rating}",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> addRatings(@PathVariable String rating, @RequestBody Movie movie, HttpServletRequest request) {
        return movieService.addRatings(rating, movie, request);
            
    }

      //update a rating to the movie for a user.
    //User is validated from the token.
    // Rating validation done in service for 0 < x > 10.
    @RequestMapping(value = "/movies/ratings/update/{rating}",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> updateRatings(@PathVariable String rating, @RequestBody Movie movie, HttpServletRequest request) {
        return movieService.updateRatings(rating, movie, request);
    }

          //delete a rating to the movie for a user.
    //User is validated from the token.
    @RequestMapping(value = "/movies/ratings/delete",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> deleteRatings(@RequestBody Movie movie, HttpServletRequest request) {
        return movieService.deleteRatings(movie, request);
    }

//    getAverage ratings for a particular movie;
    @RequestMapping(value = "/movies/ratings/get/{movieId}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> getAvgRatings(@PathVariable String movieId, HttpServletRequest request) {
        return movieService.getAvgRatings(movieId, request);
    }

    // lets get a user's personal rating to a movie so that it can be updated later.
    @RequestMapping(value = "/movies/ratings/users/get/{movieId}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> getUserRatingsForMovie(@PathVariable String movieId, HttpServletRequest request) {
        return movieService.getUserRatingsForMovie(movieId, request);
    }

//    Get a user's rating for another uer, if any.
    @RequestMapping(value = "/movies/ratings/users/{userId}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> getUserRatings(@PathVariable String userId, HttpServletRequest request) {
        return movieService.getUserRatings(userId, request);
    }

    // Add whats a new review to the movie's list
     @RequestMapping(value = "/movies/reviews/add",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> addReview(@RequestBody String body, HttpServletRequest request) {
        return movieService.addReview(body, request);
    }

    // If we want to update a user's review on movie
    @RequestMapping(value = "/movies/reviews/update",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> updateReview(@RequestBody String body, HttpServletRequest request) {
        return movieService.updateReview(body, request);
    }

    // If we want to delete a user's review on movie
    @RequestMapping(value = "/movies/reviews/delete",
            method = RequestMethod.POST,
            produces = "application/json")
    public ResponseEntity<Object> deleteReview(@RequestBody String body, HttpServletRequest request) {
       return movieService.deleteReview(body, request);
    }

    // Get Review for a particular Movie, we can use this to print this on the page.
    @RequestMapping(value = "/movies/reviews/{movieId}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> getReviewsForMovie(@PathVariable String movieId, HttpServletRequest request) {
        return movieService.getReviewsForMovie(movieId, request);
    }
    
    // If we want to find which reviews did this user gave.
    @RequestMapping(value = "/movies/reviews/users/{userId}",
            method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<Object> getReviewsForUser(@PathVariable String userId, HttpServletRequest request) {
        return movieService.getReviewsForUser(userId, request);
    }
    
}
