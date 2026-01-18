import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Login } from './auth/login/login';
import { Register } from './auth/register/register';
import { authGuard } from './core/guards/auth.guard';
import { Dashboard } from './dashboard/dashboard/dashboard';
import { ListEmployee } from './employee/list-employee/list-employee';
import { AddEmployee } from './employee/add-employee/add-employee';

export const routes: Routes = [

{ path: '', component: Home },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  {
  path: 'dashboard',
  component: Dashboard,
  canActivate: [authGuard],
  data: { roles: ['ADMIN', 'MANAGER', 'EMPLOYEE'] }
},
{
  path: 'employee/add',
  component: AddEmployee,
  canActivate: [authGuard],
  data: { roles: ['ADMIN', 'MANAGER'] }
},
{
  path: 'employee/list',
  component: ListEmployee,
  canActivate: [authGuard],
  data: { roles: ['ADMIN', 'MANAGER'] }
}

];
