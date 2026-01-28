import { Routes } from '@angular/router';

import { Home } from './home/home';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [

  // 🌐 Public routes
  { path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'register', component: Register },

  // 🧱 Dashboard (Shell + all features inside)
  {
    path: 'dashboard',
    canActivate: [authGuard],
    data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] },
    loadChildren: () =>
      import('./dashboard/dashboard-module')
        .then(m => m.DashboardModule)
  },

  // ❗ Fallback
  { path: '**', redirectTo: '' }
];
