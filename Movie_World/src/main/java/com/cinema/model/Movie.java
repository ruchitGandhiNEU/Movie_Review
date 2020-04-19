/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cinema.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Ruchit Gandhi <Ruchit at Northeasten.com>
 */
@Entity
@Table(name = "movies")
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieId;
    
    
    private String movieName;
    private int releaseYear;
    private String description;
    private String trailerLink;
    private String genre;
    private String imageLink;
    
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "watchlist",
            joinColumns = @JoinColumn(name = "movieId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<User> watchListUsers = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private Set<Ratings> ratings = new HashSet<>();
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private Set<Reviews> reviews = new HashSet<>();
    
    

    public Movie() {
    }

    public Movie(String movieName, int releaseYear, String description, String trailerLink, String genre) {
        this.movieName = movieName;
        this.releaseYear = releaseYear;
        this.description = description;
        this.trailerLink = trailerLink;
        this.genre = genre;
    }
    
    public void setAs(Movie movie){
        
        this.movieName = movie.getMovieName();
        this.releaseYear = movie.getReleaseYear();
        this.description = movie.getDescription();
        this.trailerLink = movie.getTrailerLink();
        this.genre = movie.getGenre();

        
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Set<User> getWatchListUsers() {
        return watchListUsers;
    }

    public void setWatchListUsers(Set<User> watchListUsers) {
        this.watchListUsers = watchListUsers;
    }

    public Set<Ratings> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Ratings> ratings) {
        this.ratings = ratings;
    }

    public Set<Reviews> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Reviews> reviews) {
        this.reviews = reviews;
    }
    
    

    public int getId() {
        return movieId;
    }

    public void setId(int id) {
        this.movieId = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Movie{" + "id=" + movieId + ", movieName=" + movieName + ", releaseDate=" + releaseYear + ", description=" + description + ", trailerLink=" + trailerLink + ", genre=" + genre + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Movie other = (Movie) obj;
        if (this.movieId != other.movieId) {
            return false;
        }
        return true;
    }
    
    
    
    
}