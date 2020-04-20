import {Movie} from './movie';
import {User} from './user';

export class Review {
  id: number;
  reviewId: number;
  review: String;
  reviewDate: Date;
  movieId: number;
  userId: number;
  movie: Movie;
  user: User;
}
