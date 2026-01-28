import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { AuthService } from '../../core/services/authservice';
import { MENU } from './sidenav.menu';
import { MenuItem } from '../../shared/models/menu.model';

@Component({
  selector: 'app-sidenav',
  standalone: true,
  imports: [CommonModule, RouterModule], // ✅ REQUIRED
  templateUrl: './sidenav.html',
  styleUrls: ['./sidenav.css']
})
export class SidenavComponent implements OnInit {

  role!: 'ADMIN' | 'MANAGER' | 'EMPLOYEE';
  menu: MenuItem[] = [];
  expandedIndex: number | null = null;

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
    this.role = this.auth.getRole()!;
    this.menu = MENU.filter(item =>
      item.roles.includes(this.role)
    );
  }

  toggle(index: number): void {
    this.expandedIndex = this.expandedIndex === index ? null : index;
  }
}
