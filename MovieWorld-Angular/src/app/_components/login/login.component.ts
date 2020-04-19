import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../_service/authentication.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  registerForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;
  error = '';
  error2 = '';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthenticationService) {
    if (this.authenticationService.currentUser) {
      this.router.navigate(['/']);
    }
  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
    this.registerForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      repassword: ['', Validators.required],
      firstname: ['', Validators.required],
      lastname: ['', Validators.required],
      userrole: ['', Validators.required],
      email: ['', Validators.required]
    });
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  get f() {
    return this.loginForm.controls;
  }

  get g() {
    return this.registerForm.controls;
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;

    this.authenticationService.login(this.f.username.value, this.f.password.value).subscribe(
      data => {
        this.router.navigate([this.returnUrl]);
      },
      error => {
        console.log('error obj');
        console.log(error);
        this.error = error;
        this.loading = false;
      }
    );

  }

  onRegisterSubmit() {
    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;

    this.authenticationService.register(this.g.username.value,
      this.g.password.value,
      this.g.firstname.value,
      this.g.lastname.value,
      this.g.email.value,
      this.g.userrole.value).subscribe(
              data => {
                        this.loading = false;
                        alert('Registration success, You may login');

        this.router.navigate([this.returnUrl]);
      },
      error => {
        console.log('error obj');
        console.log(error);
        this.error2 = error;
        this.loading = false;
      }

    );



  }
}
