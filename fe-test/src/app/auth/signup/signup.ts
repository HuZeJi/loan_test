import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Auth } from '../auth';

@Component({
  selector: 'app-signup',
  imports: [FormsModule, RouterLink, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  styleUrl: './signup.scss',
  templateUrl: './signup.html',
})
export class Signup {
  username = '';
  password = '';
  email = '';
  // Signal: set from an HTTP subscribe callback, needs to be a signal for the
  // zoneless app to re-render.
  error = signal('');

  constructor(private auth: Auth, private router: Router) {}

  submit() {
    this.error.set('');
    this.auth.signup(this.username, this.password, this.email).subscribe({
      next: () => this.router.navigateByUrl('/clients'),
      error: (err) => this.error.set(err.error?.message ?? 'No se pudo crear la cuenta'),
    });
  }
}
