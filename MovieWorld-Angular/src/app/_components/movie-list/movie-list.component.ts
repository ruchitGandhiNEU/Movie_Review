import { Component, OnInit } from '@angular/core';
import {MovieService} from '../../_service/movie.service';
import {Movie} from '../../_model/movie';
import {Router} from '@angular/router';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrls: ['./movie-list.component.scss']
})
export class MovieListComponent implements OnInit {
private _movies: Movie[];
private _error: String;
  constructor(private movieService: MovieService,private router: Router) {
    movieService.getAllMovies().subscribe((list) => this.movies = list ,
      error => {
        console.log('error obj -- movielist constructor');
        console.log(error);
        this._error = error;
      });
  }

  ngOnInit(): void {
  }


  get movies(): Movie[] {
    return this._movies;
  }

  set movies(value: Movie[]) {
    this._movies = value;
  }

  get error(): String {
    return this._error;
  }

  set error(value: String) {
    this._error = value;
  }

    public goToMoviePage(movie: Movie){

      this.movieService.currentOpenMovie = movie;

      this.router.navigate(['/movie-page']);

    }
}
