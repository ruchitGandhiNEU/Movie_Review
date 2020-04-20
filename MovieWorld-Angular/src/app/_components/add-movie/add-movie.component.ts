import { Component, OnInit } from '@angular/core';
import {MovieService} from '../../_service/movie.service';
import {Movie} from '../../_model/movie';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-add-movie',
  templateUrl: './add-movie.component.html',
  styleUrls: ['./add-movie.component.css']
})
export class AddMovieComponent implements OnInit {
  newMovie: Movie = new Movie();
  registerForm: FormGroup;
  loading = false;
    submitted2 = false;
      error2 = '';

  constructor(private movieService: MovieService, private formBuilder: FormBuilder) {

  }

  ngOnInit(): void {
    //     movieName : String;
    // releaseYear: number;
    // description: String;
    // trailerLink: String;
    // genre: String;
    // imageLink: String;

    this.registerForm = this.formBuilder.group({
      movieName: [null, [Validators.required]],
      releaseYear: [null, [Validators.required]],
      description: [null, [Validators.required]],
      trailerLink: [null, [Validators.required]],
      genre: [null, [Validators.required]],
      imageLink : [null]
    });
  }

  get g() {
    return this.registerForm.controls;
  }

 //  addAMovie(){
 //
 //    this.movieService.addNewMovie(this.newMovie).subscribe((data)=>{
 //      console.log('AddMovieComponent - addAMovie - subscribe - data');
 //      console.log(data);
 //    },(error)=>{
 // console.log('AddMovieComponent - addAMovie - subscribe - error');
 //      console.log(error);
 //    })
 //  }



  onNewMovieSubmit() {

    this.submitted2 = true;

    if (this.registerForm.invalid) {
      return;
    }


    this.loading = true;

        //     movieName : String;
    // releaseYear: number;
    // description: String;
    // trailerLink: String;
    // genre: String;
    // imageLink: String;

    this.newMovie.genre = this.g.genre.value;
    this.newMovie.movieName = this.g.movieName.value;
    this.newMovie.releaseYear = this.g.releaseYear.value;
    this.newMovie.description = this.g.description.value;
    this.newMovie.imageLink = this.g.imageLink.value;
    this.newMovie.trailerLink = this.g.trailerLink.value;

    this.movieService.addNewMovie(this.newMovie).subscribe(
      data => {
        this.loading = false;
        alert('Movie Added');

        this.submitted2 = false;
      },
      error => {
        console.log('error obj');
        console.log(error);
        this.error2 = error;
        this.loading = false;
        this.submitted2 = false;
      }
    );


  }




}
