import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { DASHBOARD_ROUTES } from './dashboard.routes';

// ✅ Standalone components → IMPORTED, NOT declared
import { DashboardLayout } from './layout/dashboard-layout/dashboard-layout';
import { SidenavComponent } from './sidenav/sidenav';
import { Notifications } from './notifications/notifications';
import { DashboardHome } from './home/dashboard-home/dashboard-home';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(DASHBOARD_ROUTES),

    // 👇 standalone components go here
    DashboardLayout,
    SidenavComponent,
    Notifications,
    DashboardHome
  ]
})
export class DashboardModule {}
