import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { SidenavComponent } from '../../sidenav/sidenav';
import { Notifications } from '../../notifications/notifications';

@Component({
  selector: 'app-dashboard-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,          // router-outlet
    SidenavComponent,      // <app-sidenav>
    Notifications // <app-notifications>
  ],
  templateUrl: './dashboard-layout.html',
  styleUrls: ['./dashboard-layout.css']
})
export class DashboardLayout {}
