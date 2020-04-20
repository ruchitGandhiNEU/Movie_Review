import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../_service/authentication.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from '../../_model/user';
import {ConstantClass} from '../../_model/constant-class';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent implements OnInit {
  registerForm: FormGroup;
  submitted2 = false;
  loading = false;
  returnUrl: string = '';
  error2: String = '';



  constructor(private authenticationService: AuthenticationService,
              private formBuilder: FormBuilder) {

  }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      username: [null, [Validators.required]],
      password: [null, [Validators.required]],
      repassword: [null, [Validators.required]],
      firstname: [null, [Validators.required]],
      lastname: [null, [Validators.required]],
      userrole: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$')]]
    });
    // this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  get g() {
    return this.registerForm.controls;
  }

  ValidateEmail(mail) {
    if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail)) {
      return (true);
    }
    return (false);
  }

  onRegisterSubmit() {
    // console.log(this.g.email.errors);
    this.submitted2 = true;
    if (this.g.password.value !== this.g.repassword.value) {
      alert('password and retype pasword dont match');
      this.error2 = 'Password and Retype Password does not match';
      return;
    }
    if (this.g.password.value.length < 8) {
      this.error2 = 'Password must be at least 8 char';
      return;
    }
    if (!this.ValidateEmail(this.g.email.value)) {
      this.error2 = 'Invalid Email';
    }
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
        alert('User Added');

        // this.router.navigate([this.returnUrl]);
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
