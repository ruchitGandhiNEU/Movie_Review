import { Injectable } from '@angular/core';
import {User} from '../_model/user';
import {HttpClient} from '@angular/common/http';
import { map } from 'rxjs/operators';
import {JWTResponse} from '../_model/jwtresponse';
import {Movie} from '../_model/movie';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private _currentUser: User;
  private _currentToken: String;
  private baseUrl = 'http://localhost:8084/api';

  constructor(private http: HttpClient) { }


  get currentUser(): User {
    return this._currentUser;
  }


  set currentUser(value: User) {
    this._currentUser = value;
  }

  get currentToken(): String {
    return this._currentToken;
  }

  set currentToken(value: String) {
    this._currentToken = value;
  }

  login(userName: string, password: string) {

           return this.http.post<any>(this.baseUrl + '/auth/login', {userName, password})
             .pipe(map(user => {
               // store user details and jwt token in local storage to keep user logged in between page refreshes
              let  jwtResponse : JWTResponse = user;
               localStorage.setItem('currentUser', JSON.stringify(jwtResponse.user));
               localStorage.setItem('token', JSON.stringify(jwtResponse.token));
               this.currentUser = jwtResponse.user;
               this.currentToken = jwtResponse.token;
               return user;
             }));
         }


 public logout() {
        // remove user from local storage to log user out
        console.log('logout clicked');
        localStorage.removeItem('currentUser');
        localStorage.removeItem('token');
        this._currentUser = null;
        this._currentToken = null;
        if(this._currentUser) console.log('auth service - logout - check current user = true');
          else console.log('auth service - logout - check current user = false');
        location.reload();
        return true;
    }

  public register(userName: String, password: String, firstName: String, lastName: String, email: String,userRole: String) {

    return this.http.post<any>(this.baseUrl + '/auth/register', {userName, password, firstName,lastName,email,userRole});

  }


     public changePassword(password: String) {
        return this.http.post<any>(this.baseUrl + `/auth/changepass`, { password });
    }


     public getUsersWatchList(){
        return this.http.get<any>(this.baseUrl+'/movies/watchlist/'+ this.currentUser.id);
     }
}
