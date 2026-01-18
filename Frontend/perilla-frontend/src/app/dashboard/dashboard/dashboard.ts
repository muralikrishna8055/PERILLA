import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/authservice';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule], // ✅ THIS is correct
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css'],
})
export class Dashboard implements OnInit {

  role: string | null = null;

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
    this.role = this.auth.getRole();
    console.log('TOKEN:', localStorage.getItem('perilla_auth'));
    console.log('ROLE:', this.role);
  }
}
