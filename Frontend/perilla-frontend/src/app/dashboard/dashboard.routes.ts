import { Routes } from '@angular/router';
import { DashboardLayout } from './layout/dashboard-layout/dashboard-layout';
import { DashboardHome } from './home/dashboard-home/dashboard-home';
import { authGuard } from '../core/guards/auth.guard';

export const DASHBOARD_ROUTES: Routes = [
  {
    path: '',
    component: DashboardLayout,
    canActivate: [authGuard],
    children: [
      { path: '', component: DashboardHome },

      // Employee
      { path: 'employees', loadChildren: () => import('../employee/employee-module').then(m => m.EmployeeModule) },

      // Departments (later)
      // Projects (later)
    ]
  }
];
