import { Component, OnInit } from '@angular/core';
import {MovieService} from '../../_service/movie.service';
import {Movie} from '../../_model/movie';
import {Review} from '../../_model/review';
import {HttpClient} from '@angular/common/http';
import {ConstantClass} from '../../_model/constant-class';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-movie-reviews',
  templateUrl: './movie-reviews.component.html',
  styleUrls: ['./movie-reviews.component.css']
})
export class MovieReviewsComponent implements OnInit {
    loginForm: FormGroup;
     loading = false;
    submitted = false;
  returnUrl: String = '';
  error: String = '';


private _currentOpenMovie: Movie;
private _reviews: Array<Review>;
  constructor(private movieService: MovieService, private http: HttpClient,private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router) {
    this.currentOpenMovie = movieService.currentOpenMovie;
  }

  ngOnInit(): void {
     this.loginForm = this.formBuilder.group({
            comment: ['', Validators.required]
        });
  }

     get f() { return this.loginForm.controls; }

  get currentOpenMovie(): Movie {
    return this._currentOpenMovie;
  }

  set currentOpenMovie(value: Movie) {
    this._currentOpenMovie = value;
  }

  get reviews(): Array<Review> {
    return this._reviews;
  }

  set reviews(value: Array<Review>) {
    this._reviews = value;
  }

  private getThisMovieReviews(){

    this.movieService.getThisMovieReviews(this.currentOpenMovie).subscribe((data) => {
      this.reviews = data;
    },(error)=>{})

  }

  trimming_fn(x) {
    return x ? x.replace(/^\s+|\s+$/gm, '') : '';
};

  onSubmit() {
        this.submitted = true;
        this.loading = true;


        if (this.trimming_fn(this.f.comment.value) === '' ) {
            this.error = "Comment Cannot Be Null";
            this.loading = false;
            return;}
        // stop here if form is invalid
        if (this.loginForm.invalid) {
          this.loading = false;
            return;
        }

        this.loading = true;

        let review : Review = new Review();

        this.movieService.addReview()
            // .pipe(first())
            .subscribe(
                data => {
                    this.router.navigate([this.returnUrl]);
                    this.error = "Successfully Updated, Password";
                    this.loading = false;
                },
                error => {
                    this.error = error;
                    this.loading = false;
                });

    }

}