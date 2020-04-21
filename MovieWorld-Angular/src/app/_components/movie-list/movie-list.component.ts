import {Component, OnInit} from '@angular/core';
import {MovieService} from '../../_service/movie.service';
import {Movie} from '../../_model/movie';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../_service/authentication.service';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.scss']
})
export class MovieListComponent implements OnInit {
  private _movies: Array<Movie>;
  private _error: String;
  isAdmin: boolean = false;


  constructor(private movieService: MovieService, private router: Router, private authenticateService: AuthenticationService) {
    movieService.getAllMovies().subscribe((list) => this.movies = list,
      error => {
        console.log('error obj -- movielist constructor');
        console.log(error);
        this._error = error;
      });
    this.isAdmin = authenticateService.currentUser.userRole[0] === 'A';
  }

  ngOnInit(): void {
  }


  get movies(): Array<Movie> {
    return this._movies;
  }

  set movies(value: Array<Movie>) {
    this._movies = value;
  }

  get error(): String {
    return this._error;
  }

  set error(value: String) {
    this._error = value;
  }

  public goToMoviePage(movie: Movie) {

    this.movieService.currentOpenMovie = movie;

    this.router.navigate(['/movie-page']);

  }

  deleteAMovieByAdmin(movie: Movie) {

    if (confirm('Are you sure you want to permanently delete this movie with all of its records?')) {

      this.movieService.deleteASpecificMovie(movie).subscribe((data) => {
        console.log('getMyMovieList() --> my-movie-list-component - mylist - data');
        console.log(data);

          let ind = this._movies.findIndex((m) => m.id === movie.id);
          if(ind > -1){
            this._movies.slice(ind,1);
          }


      }, (error) => {
        console.log('getMyMovieList() --> my-movie-list-component - error');
        console.log(error);
      });

    }
  }
}
