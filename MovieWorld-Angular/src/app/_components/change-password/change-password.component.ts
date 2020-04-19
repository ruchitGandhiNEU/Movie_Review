import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../_service/authentication.service';
import {User} from '../../_model/user';
import {first} from 'rxjs/operators';
import {Movie} from '../../_model/movie';
import {MovieService} from '../../_service/movie.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  loginForm: FormGroup;
     loading = false;
    submitted = false;
  _user : User;
  returnUrl: String = '';
  error: String = '';
  constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService) {
        this._user = this.authenticationService.currentUser;
            if (!this._user) {
            this.router.navigate(['/']);
        }

  }

  ngOnInit(): void {
     this.loginForm = this.formBuilder.group({
            retypepassword: ['', Validators.required],
            password: ['', Validators.required]
        });

        // get return url from route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

   get f() { return this.loginForm.controls; }

   onSubmit() {
        this.submitted = true;
        this.loading = true;


        if (this.f.retypepassword.value !== this.f.password.value) {
            this.error = "password and re-type passwords dont match";
            this.loading = false;
            return;}
        // stop here if form is invalid
        if (this.loginForm.invalid) {
          this.loading = false;
            return;
        }

        this.loading = true;
        this.authenticationService.changePassword(this.f.password.value)
            .pipe(first())
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
