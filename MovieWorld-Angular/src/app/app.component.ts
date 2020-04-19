import { Component } from '@angular/core';
import {User} from './_model/user';
import {Router} from '@angular/router';
import { AuthenticationService } from './_service/authentication.service';

@Component({
  selector: 'app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
 currentUser: User;
 authService: AuthenticationService;
 constructor(
        private router: Router,
        private authenticationService: AuthenticationService
    ) {
      this.authService = authenticationService;
      this.currentUser = this.authService.currentUser;
      localStorage.setItem("baseUrl","http://localhost:8084/api");
    }


        logout() {
        this.authenticationService.logout();
        this.router.navigate(['/login']);
    }

}
