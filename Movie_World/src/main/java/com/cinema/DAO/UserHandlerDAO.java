/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.DAO;

import com.cinema.Exceptions.UserGeneratedExceptions;
import com.cinema.model.User;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
@Component
public class UserHandlerDAO extends DAO{
    
    // Deletables
    // get == checkIfUserExistget
    // follow == followThatUser
    // unfollow == unfollowThatUser
    
    
    
    
    // Uncomment when u see usage
     public boolean checkIfUserExistget(String userName) throws UserGeneratedExceptions {
        List<User> user = getSession().createQuery("from User where userName=:userName").setString("userName", userName).list();

         return user.size() <= 0;
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
        
// Get user for userID
    public User getUser(String userId) throws UserGeneratedExceptions {
        try {
            begin();
            User user = getSession().get(User.class, Integer.parseInt(userId));
            commit();
            close();
            return user;
        } catch (HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions("USER is Invalid");
        }
    }
    
// User Registrations
    public User register(User user) throws UserGeneratedExceptions {
        try {
            begin();
            if (!checkIfUserExistget(user.getUserName())) throw new HibernateException("User Present Or alreadyExists Exists");
        
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            String bcryptPassword = bcrypt.encode(user.getPassword());
            user.setPassword(bcryptPassword);
            System.out.println(user.getFirstName());
            getSession().save(user);
            commit();
            close();
            return user;
        } catch (HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    
    
    // Search for a user using username for search operation
    public List<User> search(String userName) throws UserGeneratedExceptions {
        try {
            begin();

            Criteria check = getSession().createCriteria(User.class);
            check.add(Restrictions.like("userName", userName, MatchMode.START));
            check.setProjection(Projections.projectionList()
                    .add(Projections.property("userId").as("userId"))
                    .add(Projections.property("userName").as("userName"))
                    .add(Projections.property("firstName").as("firstName"))
                    .add(Projections.property("lastName").as("lastName"))
                    .add(Projections.property("userRole").as("userRole")));
            check.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
            List<User> list = check.list();

            commit();
            close();
            return list;
        } catch (HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    
    // Make one User Follow another
    public void followThatUser(User user, String userWantstoFollow_ID) throws UserGeneratedExceptions {
        try {
            begin();
            User followingUser = getUserFromId(userWantstoFollow_ID);
            User u = getUserFromId(String.valueOf(user.getId()));
            if (followingUser.equals(u)) {
                throw new HibernateException("You Cant Follow/Unfollow Yourself");
            }
            u.addFollowing(followingUser);
            commit();
            close();
        } catch (HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    // Unfollow one user for another user
    public void unfollowThatUser(User user, String unfollowingId) throws UserGeneratedExceptions {
        try {
            begin();
            User unfollowingUser = getUserFromId(unfollowingId);
            User u = getUserFromId(String.valueOf(user.getId()));
            if (unfollowingUser.equals(u)) throw new HibernateException("You Cant Follow/Unfollow Yourself");
            
            u.removeFollowing(unfollowingUser);
            commit();
            close();
        } catch (HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    // Get Set of all the users that I am following
    public Set<User> getFollowings(String userId) throws UserGeneratedExceptions {
        try {
            begin();
            User user = getUserFromId(userId);
            Set<User> followings = user.getFollowings();
            commit();
            close();
            return followings;
        } catch (HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    //Lets get all the users that I am following.
     public Set<User> getFollowers(String userId) throws UserGeneratedExceptions {
        try {
            begin();
            User user = getUserFromId(userId);
            Set<User> followers = user.getFollowers();
            commit();
            close();
            return followers;
        } catch (HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }

    public User changePassword(String userId, String password) throws UserGeneratedExceptions{
        
       try {
            begin();
            
           User user = getUserFromId(userId);
            if (user==null) throw new HibernateException("User Does not Exist");
        
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            String bcryptPassword = bcrypt.encode(password);
            user.setPassword(bcryptPassword);
            System.out.println(user.getFirstName() + " in userdao -  changePassword ");
            getSession().save(user);
            commit();
            close();
            return user;
        } catch (HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    
}
