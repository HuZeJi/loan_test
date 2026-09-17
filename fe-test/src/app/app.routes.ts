import { Routes } from '@angular/router';
import { authGuard } from './auth/auth-guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./auth/login/login').then((m) => m.Login) },
  { path: 'signup', loadComponent: () => import('./auth/signup/signup').then((m) => m.Signup) },
  {
    path: 'clients',
    loadComponent: () => import('./clients/clients').then((m) => m.Clients),
    canActivate: [authGuard],
  },
  {
    path: 'clients/:clientId/loans',
    loadComponent: () => import('./loans/loans').then((m) => m.Loans),
    canActivate: [authGuard],
  },
  {
    path: 'clients/:clientId/loans/:loanId/payments',
    loadComponent: () => import('./payments/payments').then((m) => m.Payments),
    canActivate: [authGuard],
  },
  { path: '', redirectTo: 'clients', pathMatch: 'full' },
  { path: '**', redirectTo: 'clients' },
];
