import { Service, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';
import { API_BASE } from '../api-base';

interface AuthResponse {
  token: string;
  tokenType: string;
  expiresInSeconds: number;
}

interface UserInfo {
  id: string;
  username: string;
  email: string;
  role: string;
}

const TOKEN_KEY = 'auth_token';

@Service()
export class Auth {
  private http = inject(HttpClient);

  loggedIn = signal(!!localStorage.getItem(TOKEN_KEY));
  userId = signal<string | null>(null);

  constructor() {
    if (this.loggedIn()) {
      queueMicrotask(() => this.loadCurrentUser());
    }
  }

  login(username: string, password: string) {
    return this.http
      .post<AuthResponse>(`${API_BASE}/auth/login`, { username, password })
      .pipe(tap((res) => this.storeToken(res.token)));
  }

  signup(username: string, password: string, email: string) {
    return this.http
      .post<AuthResponse>(`${API_BASE}/auth/signup`, { username, password, email })
      .pipe(tap((res) => this.storeToken(res.token)));
  }

  logout() {
    localStorage.removeItem(TOKEN_KEY);
    this.loggedIn.set(false);
    this.userId.set(null);
  }

  get token(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  private storeToken(token: string) {
    localStorage.setItem(TOKEN_KEY, token);
    this.loggedIn.set(true);
    this.loadCurrentUser();
  }

  private loadCurrentUser() {
    this.http.get<UserInfo>(`${API_BASE}/auth/me`).subscribe((user) => this.userId.set(user.id));
  }
}
