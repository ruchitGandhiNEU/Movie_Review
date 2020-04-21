import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Movie} from '../_model/movie';
import {User} from '../_model/user';
import {ConstantClass} from '../_model/constant-class';
import {Review} from '../_model/review';

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  private _currentOpenMovie: Movie;
  private baseUrl = localStorage.getItem("baseUrl");
  private _movies: Movie[];
  // private httpHeader = new HttpHeaders(localStorage.getItem('Content-Type'));
  constructor(private http: HttpClient) {
    // this.getAllMovies().subscribe((list) => this.movies = list );
  }

  public getAllMovies():  Observable<Array<Movie>> {
    console.log(this.baseUrl + '/movies/all'  );
    //, {headers: this.httpHeader}
    return this.http.get<Movie[]>(this.baseUrl + '/movies/all');
    // , {  headers: {
    //   'Content-Type': 'application/json'
    // }}

  }

  get movies(): Movie[] {
    return this._movies;
  }

  set movies(value: Movie[]) {
    this._movies = value;
  }

  get currentOpenMovie(): Movie {
    return this._currentOpenMovie;
  }

  set currentOpenMovie(value: Movie) {
    this._currentOpenMovie = value;
  }

  public addToWatchList(movie: Movie){

    console.log('called addToWatchList(movie) -- movie service ');

    return this.http.post(this.baseUrl + '/movies/watchlist/add', movie);

  }

  public checkIfMovieIsInWatchList(movie: Movie){

     return this.http.post(this.baseUrl + '/movies/watchlist', movie);

  }

  removeMovieFromWatchList(movie: Movie) {
    return this.http.post(this.baseUrl + '/movies/watchlist/remove', movie);
  }

  getThisMovieReviews(movie: Movie) {
   return this.http.get<Array<Review>>( ConstantClass.BaseURL + '/movies/reviews/'+movie.id );
  }

  addReview(obj: { imageLink: string; review: any; movieId: number; movieName: string }):Observable<Review> {
    return this.http.post<Review>(ConstantClass.BaseURL + '/movies/reviews/add' , obj);
  }

  addNewMovie(newMovie: Movie) {

  return this.http.post<any>(ConstantClass.BaseURL + '/movies/add', newMovie);

  }

  public getRatingForCurrentMovieAndCurrentUser() {
    // /movies/ratings/users/get/{movieId}
    return this.http.get<any>(ConstantClass.BaseURL + '/movies/ratings/users/get/'+this.currentOpenMovie.id );
  }

  addNewRatingForCurrentMovieForCurrentUser(rating: number) {
    // /movies/ratings/add/{rating}
    return this.http.post(ConstantClass.BaseURL + '/movies/ratings/add/'+rating, this.currentOpenMovie);

  }

  updateRatingForCurrentMovieForCurrentUser(rating: number) {
    // /movies/ratings/update/{rating}
    return this.http.post(ConstantClass.BaseURL + '/movies/ratings/update/'+rating, this.currentOpenMovie);
  }
}
