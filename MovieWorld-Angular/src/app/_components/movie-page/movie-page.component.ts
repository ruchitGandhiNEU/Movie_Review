import {Component, OnInit} from '@angular/core';
import {MovieService} from '../../_service/movie.service';
import {Movie} from '../../_model/movie';
import {ConstantClass} from '../../_model/constant-class';
import {first} from 'rxjs/operators';

@Component({
  selector: 'app-movie-page',
  templateUrl: './movie-page.component.html',
  styleUrls: ['./movie-page.component.css']
})
export class MoviePageComponent implements OnInit {
  private _currentOpenMovie: Movie;
  public error: String;
  public alertClass: String = ConstantClass.CLASS_ALERT_NORMAL;
  public isDisabled = false;
  public loading = false;
  public isWatchedObject: any = [
    {message: "false"}
  ];
  public isWatchlist: boolean = false;

  constructor(private movieService: MovieService) {
    this.currentOpenMovie = movieService.currentOpenMovie;
    movieService.checkIfMovieIsInWatchList(this.currentOpenMovie).subscribe((data) => {
      console.log('constructor was called from - MoviePageComponent');
      console.log(data);
      this.isWatchedObject = data;
      this.isWatchlist = this.isWatchedObject.message === "true" ;
      console.log('this.isWatchlist - ');
      console.log(this.isWatchlist);
    }, (err) => {
      console.log('Error in MoviePageComponent -   constructor - checkIfMovieIsInWatchList - subscribe');
    })
  }

  ngOnInit(): void {
  }


  get currentOpenMovie(): Movie {
    return this._currentOpenMovie;
  }

  set currentOpenMovie(value: Movie) {
    this._currentOpenMovie = value;
  }

  public addthisMovieToWatchList = () => {
    this.loading = true;
    let k = this.movieService.addToWatchList(this.currentOpenMovie);

    k.subscribe(
      data => {
        this.alertClass = ConstantClass.CLASS_ALERT_SUCCESS;
        this.error = 'Movie Added';
        this.loading = false;
        this.isDisabled = true;
        this.isWatchlist = true;
      },
      error => {
        this.alertClass = ConstantClass.CLASS_ALERT_ERROR;
        this.error = error;
        this.loading = false;
      });

    return true;

  };

  public removethisMovieFromWatchList() {

    this.movieService.removeMovieFromWatchList(this.currentOpenMovie).subscribe((data) => {
      console.log('data in MoviePageComponent  - removethisMovieFromWatchList - subscribe');
      console.log(data);
      this.isWatchlist = false;
    }, (err) => {
      console.log('Error in MoviePageComponent  - removethisMovieFromWatchList - subscribe');
    });

  }
}
