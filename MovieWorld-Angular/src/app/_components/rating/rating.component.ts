import {Component, OnInit} from '@angular/core';
import {MovieService} from '../../_service/movie.service';


export class Item {
  name: string;
  value: number;
  checked: boolean;
}


@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.css']
})
export class RatingComponent implements OnInit {


  userRatingVal: number;
  userAction : String;

  constructor(private movieService: MovieService) {

    this.getRatingForCurrentMovieAndCurrentUser();

  }

  ngOnInit(): void {

  }



  public getRatingForCurrentMovieAndCurrentUser() {
    // /movies/ratings/users/get/{movieId}
    this.movieService.getRatingForCurrentMovieAndCurrentUser().subscribe((data)=>{
      let obj : { rating: number, action:String };
      obj = data;
      console.log('obj and data from rating component');
      console.log(obj);
      console.log(data);
      this.userRatingVal = obj.rating;
      this.userAction = obj.action;
      console.log('this.userRatingVal  = '+this.userRatingVal +'  this.userAction = '+ this.userAction );
    },(error) => {
      console.log('getRatingForCurrentMovieAndCurrentUser - RatingComponent - error');
      console.log(error);
    });

  }

  getCurrentUserRatingForCurrentMovie(){


  }


  onItemChange(val: any, e: Event) {
    e.preventDefault();
        if (val === this.userRatingVal) {
      return;
    }

        console.log('when clicked, new rating is - > '+ val);


    if(this.userAction === 'add'){

      this.movieService.addNewRatingForCurrentMovieForCurrentUser(val).subscribe((data)=>{
        console.log('in RatingComponent - onItemChange - addNewRatingForCurrentMovieForCurrentUser - data ');
        console.log(data);
        this.userRatingVal = val;
        console.log('updating userRatingVal to = '+ this.userRatingVal);
        this.userAction = 'update';
      },(error)=>{
        console.log('in RatingComponent - onItemChange - addNewRatingForCurrentMovieForCurrentUser - error ');
        console.log(error);
      });

    }

        if(this.userAction === 'update'){

      this.movieService.updateRatingForCurrentMovieForCurrentUser(val).subscribe((data)=>{
        this.userRatingVal = val;
        this.userAction = 'update';
      },(error)=>{
        console.log('in RatingComponent - onItemChange - updateRatingForCurrentMovieForCurrentUser - error ');
        console.log(error);
      })
    }

  }

}
