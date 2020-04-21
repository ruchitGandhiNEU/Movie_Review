import {Component, OnInit} from '@angular/core';
import {MovieService} from '../../_service/movie.service';
import {Movie} from '../../_model/movie';
import {Review} from '../../_model/review';
import {HttpClient} from '@angular/common/http';
import {ConstantClass} from '../../_model/constant-class';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../_service/authentication.service';

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
  isCritic: boolean = false;
  isAdmin: boolean;

  private _currentOpenMovie: Movie;
  private _reviews: Array<Review>;


  constructor(private movieService: MovieService, private authenticateService: AuthenticationService, private http: HttpClient, private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router) {
    this.currentOpenMovie = movieService.currentOpenMovie;
    this.getThisMovieReviews();
    this.isAdmin = authenticateService.currentUser.userRole[0] === 'A';
    this.isCritic = authenticateService.currentUser.userRole[0] === 'C';
    console.log('iscritic = ' + this.isCritic + ' isAdmin = ' + this.isAdmin);


  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      comment: ['', Validators.required]
    });
  }

  get f() {
    return this.loginForm.controls;
  }

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

  private getThisMovieReviews() {

    this.movieService.getThisMovieReviews(this.currentOpenMovie).subscribe((data) => {
      this.reviews = data;
      console.log('getting reviews for this movie --');
      console.log(data);
      console.log('getting reviews for this movie -- this.reviews object is like -- ');
      console.log(this.reviews);


    }, (error) => {
    });

  }

  trimming_fn(x) {
    return x ? x.replace(/^\s+|\s+$/gm, '') : '';
  };

  onSubmit() {
    this.submitted = true;
    this.loading = true;


    if (this.trimming_fn(this.f.comment.value) === '') {
      this.error = 'Comment Cannot Be Null';
      this.loading = false;
      return;
    }
    // stop here if form is invalid
    if (this.loginForm.invalid) {
      this.loading = false;
      return;
    }

    this.loading = true;


    // REVIEW MOVIEID MOVIENAME IMAGELINK
    const obj = {
      review: this.f.comment.value,
      movieId: this.currentOpenMovie.id.valueOf(),
      movieName: this.currentOpenMovie.movieName.valueOf(),
      imageLink: this.currentOpenMovie.imageLink.valueOf()
    };
    this.movieService.addReview(obj)
      // .pipe(first())
      .subscribe(
        data => {
          let NewReview: Review = data;
          console.log('new review');
          console.log(NewReview);
          this._reviews.push(NewReview);
          this.router.navigate([this.returnUrl]);
          this.error = 'Review Added';
          this.loading = false;
        },
        error => {
          this.error = error;
          this.loading = false;
        });

  }

  public deleteThisReview(review: Review) {
    if (confirm('Are You sure you want to delete this ?')) {

      this.movieService.deleteThisReview(review).subscribe((data) => {
          // alert(data);
          let ind = this._reviews.findIndex((r) => r.id === review.id);
          if (ind > -1) {
            this._reviews.splice(ind, 1);
          }

        },
        (error) => {
          console.log('Error: deleteThisReview - MovieReviewsComponent ');
          console.log(error);
        });


    }

  }

}
