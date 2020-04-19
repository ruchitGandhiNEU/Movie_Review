import { Component, OnInit } from '@angular/core';
import {User} from '../../_model/user';
import {UserService} from '../../_service/user.service';
import {AuthenticationService} from '../../_service/authentication.service';
import {ActivatedRoute} from '@angular/router';
import {LocationStrategy} from '@angular/common';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  loading = false;
  private _currentUser: User;

  constructor(private userService: UserService, private authenticateService: AuthenticationService,public route: ActivatedRoute, private locationStrategy: LocationStrategy) {
    this.currentUser = authenticateService.currentUser;
  }

  ngOnInit(): void {

         history.pushState(null, null, location.href);
        this.locationStrategy.onPopState(() => {
          history.pushState(null, null, location.href);
        })

  }


  get currentUser(): User {
    return this._currentUser;
  }

  set currentUser(value: User) {
    this._currentUser = value;
  }

  public logout() {
    console.log('home-component logout() called');
   return this.authenticateService.logout();

  }
}
