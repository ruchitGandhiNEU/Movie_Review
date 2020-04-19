import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import { AppComponent } from './app.component';
import { appRoutingModule } from './app.routing';
import {JwtInterceptor, AuthGuard, ErrorInterceptor} from './_interceptor/';
import { HomeComponent } from './_components/home/home.component';
import { LoginComponent } from './_components/login/login.component';
import { MovieListComponent } from './_components/movie-list/movie-list.component';
import { UserProfileComponent } from './_components/user-profile/user-profile.component';
import { ChangePasswordComponent } from './_components/change-password/change-password.component';
import { MoviePageComponent } from './_components/movie-page/movie-page.component';
import { MyMovieListComponent } from './_components/my-movie-list/my-movie-list.component';
import { MovieReviewsComponent } from './_components/movie-reviews/movie-reviews.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    MovieListComponent,
    UserProfileComponent,
    ChangePasswordComponent,
    MoviePageComponent,
    MyMovieListComponent,
    MovieReviewsComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
    appRoutingModule
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
