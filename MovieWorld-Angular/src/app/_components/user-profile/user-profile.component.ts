import { Component, OnInit } from '@angular/core';
import {UserService} from '../../_service/user.service';
import {AuthenticationService} from '../../_service/authentication.service';
import {ActivatedRoute} from '@angular/router';
import {User} from '../../_model/user';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

   private _currentUser: User;

  constructor(private userService: UserService, private authenticateService: AuthenticationService,public route: ActivatedRoute) {

    this.currentUser = authenticateService.currentUser;
  }

  ngOnInit(): void {
  }


  get currentUser(): User {
    return this._currentUser;
  }

  set currentUser(value: User) {
    this._currentUser = value;
  }
}
