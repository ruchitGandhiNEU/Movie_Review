/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.DAO;


import com.cinema.Exceptions.UserGeneratedExceptions;
import com.cinema.model.User;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
@Component
public class AuthorizationDAO extends DAO{
    
      // User authentication
    public User authenticate(String userName, String password) throws UserGeneratedExceptions {
        try {
            begin();
            
            List<User> list= getSession().createQuery("FROM User WHERE userName=:userName").setString("userName", userName).list();

            if(list.isEmpty()) throw new Exception("Invalid UserName or Password");
    
            User user = list.get(0);
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            if(!bcrypt.matches(password, user.getPassword()))
                throw new Exception("Invalid UserName or Password");
            commit();
            close();
            return user;
        } catch (Exception e) {
            rollback();
            throw new UserGeneratedExceptions(e.getMessage());
        }
    }
    
    // Getting user details from userId
    public User getUser(String userId) throws UserGeneratedExceptions{
        try {
            begin();
            User user = getSession().get(User.class, Integer.parseInt(userId));
            commit();
            close();
            return user;
        } catch (HibernateException e) {
            rollback();
            throw new UserGeneratedExceptions("User does not exist");
        }
    }
    
}
