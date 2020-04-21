/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.service;

import com.cinema.DAO.MovieHandlerDAO;
import com.cinema.Exceptions.ErrorsResponse;
import com.cinema.Exceptions.UserGeneratedExceptions;
import com.cinema.model.CustomMessage;
import com.cinema.model.Movie;
import com.cinema.model.Reviews;
import com.cinema.model.Role;
import com.cinema.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
@Component
public class MovieService {
    
    
    
    @Autowired
    @Qualifier("movieDao")
    MovieHandlerDAO movieDao;
   
    public  ResponseEntity<Object> getAllMovies() throws UserGeneratedExceptions{
        List<Movie> allMovies = null;
        try {
                 allMovies = movieDao.getAllMovies();
            return new ResponseEntity<>(allMovies, HttpStatus.OK);
        } catch (Exception e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }
    
    
    
        

    public  ResponseEntity<Object> deleteAMovie(Movie movie,HttpServletRequest request) throws UserGeneratedExceptions{
        
        try {
                  movieDao.deleteAMovie(movie);
            return new ResponseEntity<>("Movie Deleted Successfully", HttpStatus.OK);
        } catch (Exception e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    
        
        
    }
    
    
    

    public  ResponseEntity<Object> addNewMovie(Movie movie,HttpServletRequest request) throws UserGeneratedExceptions{
       
        try {
            
               movieDao.addNewMovie(movie);

            return new ResponseEntity<>("Movie Added", HttpStatus.OK);
        } catch (Exception e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
        
    }
    
    

    public ResponseEntity<Object> addToWatchList(Movie movie, HttpServletRequest request) throws Exception {
        User user = null;
        try {
            user = (User) request.getAttribute("user");
            movieDao.addToWatchList(user, movie);
            return new ResponseEntity<>(new CustomMessage("Movie added to watchlist"), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }

    
    public ResponseEntity<Object> removeFromWatchList(Movie movie, HttpServletRequest request) throws Exception {
        User user = null;
        try {
            user = (User) request.getAttribute("user");
            movieDao.removeFromWatchList(user, movie);
            return new ResponseEntity<>(new CustomMessage("Movie removed from watchlist"), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Object> isWatchList(Movie movie, HttpServletRequest request) throws Exception {
        User user = null;
        try {
            user = (User) request.getAttribute("user");
            boolean isWatchList;
            if (user == null) {
                System.out.println("com.cinema.controller.MovieController.isWatchList() -- user is null");
                isWatchList = false;
            } else {
                
                System.out.println("com.cinema.controller.MovieController.isWatchList() -- user is not null going for DAO");
                isWatchList = movieDao.isWatchList(movie, user);
                System.out.println("com.cinema.controller.MovieController.isWatchList() -- DAO returned isWatchlist as - "+ isWatchList);                
            }
            return new ResponseEntity<>(new CustomMessage(String.valueOf(isWatchList)), HttpStatus.OK);
        } catch (Exception e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }

    
    public ResponseEntity<Object> getWatchList(String userId) {
        try {
            Set<Movie> watchlist = movieDao.getWatchList(userId);
            return new ResponseEntity<>(watchlist, HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }

    
    public ResponseEntity<Object> addRatings(String rating, Movie movie, HttpServletRequest request) {
        try {
            double r = Double.parseDouble(rating);
            if (r < 0 || r > 10) {
                throw new UserGeneratedExceptions("Rating should be in range of 0 to 10");
            }
            User user = (User) request.getAttribute("user");
            movieDao.addRatings(movie, user, r);
            return new ResponseEntity<>(new CustomMessage("Ratings added"), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage("Invalid ratings"));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> updateRatings(String rating, Movie movie, HttpServletRequest request) {
        try {
            double r = Double.parseDouble(rating);
            if (r < 0 || r > 10) {
                throw new UserGeneratedExceptions("Please let ratings be between Zero and Ten");
            }
            User user = (User) request.getAttribute("user");
            movieDao.updateRatings(movie, user, r);
            return new ResponseEntity<>(new CustomMessage("Successfully updated"), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage("Invalid ratings"));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> deleteRatings(Movie movie, HttpServletRequest request) {
        try {
            User user = (User) request.getAttribute("user");
            movieDao.deleteRatings(movie, user);
            return new ResponseEntity<>(new CustomMessage("Ratings deleted"), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> getAvgRatings(String movieId, HttpServletRequest request) {
        try {
            double avgRatings = movieDao.getAvgRatings(movieId);
            return new ResponseEntity<>(new CustomMessage(String.valueOf(avgRatings)), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> getUserRatingsForMovie(String movieId, HttpServletRequest request) {
        try {
            User user = (User) request.getAttribute("user");
            JSONObject ratings = movieDao.getUserRatingsForMovie(movieId, user);

            return new ResponseEntity<>(ratings.toMap(), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> getUserRatings(String userId, HttpServletRequest request) {
        try {
//            User user = (User) request.getAttribute("user");
            List<Object> ratings = movieDao.getUserRatings(userId);

            return new ResponseEntity<>(ratings, HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }



    public ResponseEntity<Object> addReview(String body, HttpServletRequest request) {
        try {
            User user = (User) request.getAttribute("user");
            
            if (user.getUserRole() == Role.User) {
                throw new UserGeneratedExceptions("User is not a critic");
            }
            JSONObject json = new JSONObject(body);
                if (json.getString("review").equals("")) {
                throw new UserGeneratedExceptions("Review is empty text");
            }
            Movie m = new Movie();
            m.setId(json.getInt("movieId"));
            m.setMovieName(json.getString("movieName"));
            m.setImageLink(json.getString("imageLink"));
            Reviews r =movieDao.addReview(user, m, json.getString("review"));

            return new ResponseEntity<>(r, HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> updateReview( String body, HttpServletRequest request) {
        try {
            User user = (User) request.getAttribute("user");
            if (user.getUserRole() == Role.User) {
                throw new UserGeneratedExceptions("User is unauthorized");
            }
            JSONObject json = new JSONObject(body);
            if (json.getString("review").equals("")) {
                throw new UserGeneratedExceptions("Review is empty text");
            }

            movieDao.updateReview(user, json.getInt("reviewId"), json.getString("review"));

            return new ResponseEntity<>(new CustomMessage("Review updated"), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> deleteReview(String body, HttpServletRequest request) {
        try {
            User user = (User) request.getAttribute("user");
            if (user.getUserRole() == Role.User) {
                throw new UserGeneratedExceptions("User is unauthorized");
            }
            
            JSONObject json = new JSONObject(body);

            movieDao.deleteReview(user, json.getInt("reviewId"));

            return new ResponseEntity<>(new CustomMessage("Review deleted"), HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<Object> getReviewsForMovie(String movieId, HttpServletRequest request) {
        try {

            List<Reviews> list = movieDao.getReviewsForMovie(movieId);

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }
    

    public ResponseEntity<Object> getReviewsForUser(String userId, HttpServletRequest request) {
        try {

            List<Reviews> list = movieDao.getReviewsForUser(userId);

            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (UserGeneratedExceptions e) {
            List<CustomMessage> errors = new ArrayList<>();
            errors.add(new CustomMessage(e.getMessage()));
            return new ResponseEntity<>(new ErrorsResponse(errors), HttpStatus.BAD_REQUEST);
        }
    }
    
}
