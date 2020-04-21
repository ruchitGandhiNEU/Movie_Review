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
  submitted2 = false;
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
      username: [null, [Validators.required]],
      password: [null, [Validators.required]]
    });
    this.registerForm = this.formBuilder.group({
      username: [null, [Validators.required, Validators.pattern('^[a-z0-9_-]{3,16}$')]],
      password: [null, [Validators.required]],
      repassword: [null, [Validators.required]],
      firstname: [null, [Validators.required]],
      lastname: [null, [Validators.required]],
      userrole: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$')]]
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
    this.submitted = true;
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;

    this.authenticationService.login(this.f.username.value, this.f.password.value).subscribe(
      data => {
        this.router.navigate([this.returnUrl]);
        this.submitted = false;
      },
      error => {
        console.log('error obj');
        console.log(error);
        this.error = error;
        this.loading = false;
        this.submitted = false;

      }
    );

  }

  ValidateEmail(mail) {
    if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail)) {
      return (true);
    }
    return (false);
  }


  onRegisterSubmit() {
    console.log(this.g.email.errors);
    this.submitted2 = true;
        if (this.g.password.value !== this.g.repassword.value) {
      alert("password and retype pasword dont match");
      this.error2 = 'Password and Retype Password does not match';
      return;
    }
        if(this.g.password.value.length < 8) this.error2 = 'Password must be atleast 8 char';
        if(!this.ValidateEmail(this.g.email.value)) this.error2 = 'Invalid Email';
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
        this.submitted2 = false;
      },
      error => {
        console.log('error obj');
        console.log(error);
        this.error2 = error;
        this.loading = false;
        this.submitted2 = false;
      }
    );


  }
}
