/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.filter;

import com.cinema.TokenResponse.JWTTokenUtility;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.cinema.DAO.AuthorizationDAO;
import com.cinema.Exceptions.ErrorsResponse;
import com.cinema.model.CustomMessage;
import com.cinema.model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.util.Enumeration;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
@Component
public class JWTFilter extends OncePerRequestFilter {

    private JWTTokenUtility jwtTokenUtility = new JWTTokenUtility();

//
//    public JWTTokenUtility getJwtTokenUtility() {
//        return jwtTokenUtility;
//    }
//
//    public void setJwtTokenUtility(JWTTokenUtility jwtTokenUtility) {
//        this.jwtTokenUtility = jwtTokenUtility;
//    }
    
     AuthorizationDAO authDao = new AuthorizationDAO();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        String header = request.getHeader("Authorization");

        String userId = null;
        String token = null;
        try {
            if (header != null && header.startsWith("Bearer ")) {

                token = header.substring(7);

                System.out.println("com.movies.movieworld.filter.JWTFilter.doFilterInternal() - token - " + token);

                if (jwtTokenUtility == null) {
                    System.err.println("jwtTokenUtility == null - true");
                }

                userId = jwtTokenUtility.getUserIdFromToken(token);

                System.out.println("com.movies.movieworld.filter.JWTFilter.doFilterInternal() - user id from token - " + userId);

            } else {
                generateErrorResponse(response);
            }

            if (userId != null && jwtTokenUtility.validateToken(token)) {
                User u = authDao.getUser(userId);
                if(u==null) generateErrorResponse(response);
                request.setAttribute("user", u);
//                System.out.println("com.movies.movieworld.filter.JWTFilter.doFilterInternal() - validateToken - true");
            } else {
                generateErrorResponse(response);
            }

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            System.out.println("com.movies.movieworld.filter.JWTFilter.doFilterInternal() - Exception - " + e.getMessage());
            e.printStackTrace();
            generateErrorResponse(response);
        }

    }

    void generateErrorResponse(HttpServletResponse response) throws IOException {
        try{
             System.err.println("INSIDE of generateErrorResponse - JWTFilter");
        ObjectMapper mapper = new ObjectMapper();
        List<CustomMessage> errors = new ArrayList<>();
        errors.add(new CustomMessage("Invalid Token or Unauthorized Access"));
        String Json = mapper.writeValueAsString(new ErrorsResponse(errors));
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(Json);
        }catch(Exception e){
            System.err.println("Exception of generateErrorResponse - JWTFilter");
        e.printStackTrace();
        }
    }
}
