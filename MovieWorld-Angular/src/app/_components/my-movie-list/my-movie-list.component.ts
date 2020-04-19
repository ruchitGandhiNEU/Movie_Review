import { Component, OnInit } from '@angular/core';
import {AuthenticationService} from '../../_service/authentication.service';
import {MovieService} from '../../_service/movie.service';
import {Movie} from '../../_model/movie';
import {Router} from '@angular/router';

@Component({
  selector: 'app-my-movie-list',
  templateUrl: './my-movie-list.component.html',
  styleUrls: ['./my-movie-list.component.css']
})
export class MyMovieListComponent implements OnInit {
  public myMovies : Array<Movie>;
  public isListEmpty : boolean = true;
  public error : any = "";
  constructor(private authenticateService: AuthenticationService, private movieService: MovieService, private router: Router) {
    this.getMyMovieList();
    this.isListEmpty= this.myMovies===null;
    console.log('this.isListEmpty - '+this.isListEmpty);

  }

  ngOnInit(): void {
  }


  public getMyMovieList(){

    this.authenticateService.getUsersWatchList().subscribe(
      (data) => {
        console.log('getMyMovieList() --> my-movie-list-component - mylist - data');
        console.log(data);
        this.myMovies = data;

    },(error) => {
        console.log('getMyMovieList() --> my-movie-list-component - error');
      console.log(error);

    });

  }

      public goToMoviePage(movie: Movie){

      this.movieService.currentOpenMovie = movie;

      this.router.navigate(['/movie-page']);

    }

}
