import { Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { Auth } from './auth/auth';

@Component({
  imports: [RouterOutlet, RouterLink, MatToolbarModule, MatButtonModule],
  selector: 'app-root',
  styleUrl: './app.scss',
  templateUrl: './app.html',
})
export class App {
  constructor(protected auth: Auth, private router: Router) {}

  logout() {
    this.auth.logout();
    this.router.navigateByUrl('/login');
  }
}
