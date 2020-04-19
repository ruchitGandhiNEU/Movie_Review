import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthenticationService} from '../_service/authentication.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) {
  }

  intercept(request, next) {

    let currentUser = this.authenticationService.currentUser;
    let token = this.authenticationService.currentToken;
    console.log(currentUser);
    console.log(token);
    let pq;
    pq = request.clone({
      setHeaders: {Authorization: `Bearer ${token}`}

    });

    // let k = request.headers.set("x-auth-token",'Bearer '+token);
    // console.log(' printing request in - JwtInterceptor - intercept - (k) requesst?=  ');
    // console.log(k);
    console.log(' printing request in - JwtInterceptor - intercept - [r] requesst?=  ' + request.headers.has('x'));
    console.log(pq);

    return next.handle(pq );
  }
}
