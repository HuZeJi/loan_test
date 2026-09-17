import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { Auth } from '../auth';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  styleUrl: './login.scss',
  templateUrl: './login.html',
})
export class Login {
  username = '';
  password = '';
  error = signal('');

  constructor(private auth: Auth, private router: Router) {}

  submit() {
    this.error.set('');
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigateByUrl('/clients'),
      error: () => this.error.set('Usuario o contraseña invalidos'),
    });
  }
}
