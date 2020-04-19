/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.DAO;

import com.cinema.Exceptions.UserGeneratedExceptions;
import com.cinema.model.Movie;
import com.cinema.model.Ratings;
import com.cinema.model.Reviews;
import com.cinema.model.User;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.criteria.CriteriaBuilder;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
@Component
public class MovieHandlerDAO extends DAO{
    
public List<Movie> getAllMovies() throws UserGeneratedExceptions{
try {
            begin();
            
            CriteriaBuilder builder = getSession().getCriteriaBuilder();
            String hql = "FROM Movie";
            Query query = getSession().createQuery(hql);
            List<Movie> movies = query.list();

            
            commit();
            close();
            return movies;
        } catch(HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }

}

public Movie getMovieFromID(int id) throws UserGeneratedExceptions{
try {

            return getSession().get(Movie.class, id);
            
        } catch(HibernateException e) {
            System.out.println("in MovieHandlerDAO - getMovieFromID - Exception " + e.getMessage());
            throw new UserGeneratedExceptions(e.getMessage());
        }

}


// Get a user uing ID, not implementing begin close as it will be called by other methods
        public User getUserFromId(String userId) throws UserGeneratedExceptions {
        try {
            return getSession().get(User.class, Integer.parseInt(userId));
        } catch (HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions("Invalid USER");
        }
    }
        
   // Get a user uing ID, not implementing begin close as it will be called by other methods
        public User getUserFromId(int userIdInt) throws UserGeneratedExceptions {
        try {
            return getSession().get(User.class, userIdInt);
        } catch (HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions("Invalid USER");
        }
    }
    
    public Movie addOrGetMovie(Movie movie, String action) {
        try {
            Movie m = getSession().get(Movie.class, movie.getId());
            if (m == null) {
                throw new HibernateException("New Movie");
            }
            return m;
        } catch (HibernateException e) {
            if (action.equals("get")) {
                return null;
            } else {
                Movie m = new Movie();
                m.setId(movie.getId());
                m.setMovieName(movie.getMovieName());
                m.setImageLink(movie.getImageLink());
                getSession().save(m);
                return m;
            }

        }
    }
    
    //Add user to watchList
    public void addToWatchList(User user, Movie movie) throws UserGeneratedExceptions {
        try {
            begin();
            Movie m = addOrGetMovie(movie, "add");
            User u = getUserFromId(user.getId());
            m.getWatchListUsers().add(u);
            
            commit();
            close();
        } catch (UserGeneratedExceptions e) {
            rollback();
            throw new UserGeneratedExceptions("Movie not added to watchlist");
        }
    }
    
    
    
    public void removeFromWatchList(User user, Movie movie) throws UserGeneratedExceptions {
        try {
            begin();
            Movie m = addOrGetMovie(movie, "remove");
            User u = getUserFromId(user.getId());
            m.getWatchListUsers().remove(u);
            commit();
            close();
        } catch (Exception e) {
            rollback();
            throw new UserGeneratedExceptions("Movie not removed from watchlist");
        }
    }

    public boolean isWatchList(Movie movie, User user) throws UserGeneratedExceptions {
        try {
            begin();
            Movie m = addOrGetMovie(movie, "get");
            if (m == null) {
                commit();
                close();
                return false;
            }
            Set<User> watchlist = m.getWatchListUsers();
            boolean val = false;
            User u = getUserFromId(user.getId());
            if(watchlist.contains(u))
                val = true;

            commit();
            close();
            return val;
        } catch (Exception e) {
            rollback();
            throw new UserGeneratedExceptions("No movie found");
        }
    }

    public Set<Movie> getWatchList(String userId) throws UserGeneratedExceptions {
        try {
            begin();
            User user = getUserFromId(Integer.parseInt(userId));
            Set<Movie> movies = user.getWatchlist();
            commit();
            close();
            return movies;
        } catch (UserGeneratedExceptions e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    // Add ratings for movie
    public void addRatings(Movie movie, User user, double rating) throws UserGeneratedExceptions {
        try {
            begin();
            Movie m = addOrGetMovie(movie, "add");
            User u = getUserFromId(user.getId());
            Ratings r = new Ratings();
            r.setRating(rating);
            r.setMovie(m);
            r.setUser(u);
            m.getRatings().add(r);
            commit();
            close();
        } catch(UserGeneratedExceptions e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        } 
    }
    
    // Update ratings for movie
    public void updateRatings(Movie movie, User user, double rating) throws UserGeneratedExceptions {
        try {
            begin();
            String hql = "UPDATE Ratings r set rating=:rating WHERE movie.movieId=:movieId AND user.userId=:userId";
            Query query = getSession().createQuery(hql);
            query.setParameter("rating", rating);
            query.setParameter("movieId", movie.getId());
            query.setParameter("userId", user.getId());
            int result = query.executeUpdate();
            commit();
            close();
        } catch(HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        } 
    }

    // Delete ratings for movie
    public void deleteRatings(Movie movie, User user) throws UserGeneratedExceptions {
        try {
            begin();
            String hql = "DELETE FROM Ratings r WHERE movie.movieId=:movieId AND user.userId=:userId";
            Query query = getSession().createQuery(hql);
            query.setParameter("movieId", movie.getId());
            query.setParameter("userId", user.getId());
            int result = query.executeUpdate();
            commit();
            close();
        } catch(HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        } 
    }
    
    // Get average ratings
    public double getAvgRatings(String movieId) throws UserGeneratedExceptions {
        try {
            begin();
            String hql = "SELECT AVG(r.rating) AS rating FROM Ratings r WHERE movie.movieId=:movieId";
            Query query = getSession().createQuery(hql);
            query.setParameter("movieId", Integer.parseInt(movieId));
//            Criteria r = getSession().createCriteria(Ratings.class);
//            r.add(Restrictions.eq("movie.movieId", Integer.parseInt(movieId)));
//            ProjectionList proj = Projections.projectionList();
//            proj.add(Projections.avg("rating"));
//            r.setProjection(proj);
            double rating;
            if(query.list().get(0) == null)
                rating = 0;
            else
                rating = (double) query.list().get(0);
            commit();
            close();
            return rating;
        } catch(HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        } 
    }

    // Get user rating for User
    public JSONObject getUserRatingsForMovie(String movieId, User user) throws UserGeneratedExceptions {
        try {
            begin();
            String hql = "SELECT r.rating as rating "
                    + "FROM Ratings r JOIN Movie m ON r.movie.movieId = m.movieId "
                    + "WHERE r.user.userId=:userId AND m.movieId=:movieId";
            Query query = getSession().createQuery(hql);
            query.setParameter("userId", user.getId());
            query.setParameter("movieId", Integer.parseInt(movieId));
            double rating;
            JSONObject json = new JSONObject();
            
            if(query.list().isEmpty()) {
                rating = 0;
                json.put("rating", rating);
                json.put("action", "add");
            } else {
                rating = (double) query.list().get(0);
                json.put("rating", rating);
                json.put("action", "update");
            }
                
            commit();
            close();
            return json;
        } catch(HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    // Get User Ratings
    public List<Object> getUserRatings(String userId) throws UserGeneratedExceptions {
        try {
            begin();
            String hql = "SELECT m.movieId AS movieId, m.movieName as movieName, m.movieImage AS movieImage, r.rating as rating "
                    + "FROM Ratings r JOIN Movie m ON r.movie.movieId = m.movieId "
                    + "WHERE r.user.userId=:userId";
            Query query = getSession().createQuery(hql);
            query.setParameter("userId", Integer.parseInt(userId));
            List<Object> ratings = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
            commit();
            close();
            return ratings;
        } catch(HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }

    // Add review for movie
    public Reviews addReview(User user, Movie movie, String review) throws UserGeneratedExceptions  {
        try {
            begin();
            User u = getUserFromId(user.getUserId());
            Movie m = addOrGetMovie(movie, "add");
            Reviews r = new Reviews();
            r.setReview(review);
            r.setReviewDate(new Date());
            r.setMovie(m);
            r.setUser(u);
            getSession().save(r);
            commit();
            close();
            return r;
        } catch(HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    // Update review for movie
    public void updateReview(User user, int reviewId, String review) throws UserGeneratedExceptions  {
        try {
            begin();
            User u = getUserFromId(user.getId());
            String hql = "UPDATE Reviews r SET review=:review WHERE r.reviewId=:reviewId AND r.user.userId=:userId";
            Query query = getSession().createQuery(hql);
            query.setParameter("review", review);
            query.setParameter("reviewId", reviewId);
            query.setParameter("userId", u.getId());
            
            if(query.executeUpdate() == 0)
                throw new UserGeneratedExceptions("Not updated or review not found");
            commit();
            close();
        } catch(UserGeneratedExceptions e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    // Delete review for movie
    public void deleteReview(User user, int reviewId) throws UserGeneratedExceptions  {
        try {
            begin();
            User u = getUserFromId(user.getId());
            String hql = "DELETE FROM Reviews r WHERE reviewId=:reviewId AND user.userId=:userId";
            Query query = getSession().createQuery(hql);
            query.setParameter("reviewId", reviewId);
            query.setParameter("userId", u.getId());
            
            if(query.executeUpdate() == 0)
                throw new UserGeneratedExceptions("Not deleted or review not found");
            commit();
            close();
        } catch(UserGeneratedExceptions e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    // Get Reviews for movie
    public List<Reviews> getReviewsForMovie(String movieId) throws UserGeneratedExceptions {
        try {
            begin();
            String hql = "FROM Reviews WHERE movie.movieId=:movieId ORDER BY reviewDate DESC";
            Query query = getSession().createQuery(hql);
            query.setParameter("movieId", Integer.parseInt(movieId));
            
            List<Reviews> reviews = query.list();

            
            commit();
            close();
            return reviews;
        } catch(HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    // Get Reviews for Users
    public List<Reviews> getReviewsForUser(String userId) throws UserGeneratedExceptions {
        try {
            begin();
            String hql = "FROM Reviews WHERE user.userId=:userId ORDER BY reviewDate DESC";
            Query query = getSession().createQuery(hql);
            query.setParameter("userId", Integer.parseInt(userId));
            
            List<Reviews> reviews = query.list();

            commit();
            close();
            return reviews;
        } catch(HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
}
