import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './_components/home/home.component';
import { LoginComponent } from './_components/login/login.component';
import { AuthGuard } from './_interceptor/auth.guard';
import {MovieListComponent} from './_components/movie-list/movie-list.component';
import {UserProfileComponent} from './_components/user-profile/user-profile.component';
import {ChangePasswordComponent} from './_components/change-password/change-password.component';
import {MoviePageComponent} from './_components/movie-page/movie-page.component';
import {MyMovieListComponent} from './_components/my-movie-list/my-movie-list.component';

const routes: Routes = [
    { path: '', component: HomeComponent, canActivate: [AuthGuard],
    children: [
      {path: 'movieslist', component: MovieListComponent},
      { path: 'profile', component: UserProfileComponent},
      {path: 'change-password', component: ChangePasswordComponent},
      { path: 'movie-page', component: MoviePageComponent},
      { path: 'my-movies', component: MyMovieListComponent},
      {path : '', redirectTo: 'movieslist', pathMatch: 'full'}
    ]},
    { path: 'login', component: LoginComponent },

    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

export const appRoutingModule = RouterModule.forRoot(routes);
