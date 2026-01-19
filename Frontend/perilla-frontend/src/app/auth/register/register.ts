import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  organizationName = '';
  tenantCode = '';
  adminUsername = '';
  password = '';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  onRegister(): void {

    const payload = {
      organizationName: this.organizationName,
      tenantCode: this.tenantCode,
      adminUsername: this.adminUsername,
      password: this.password
    };

    this.http.post(
      'http://localhost:8081/api/auth/register',
      payload
    ).subscribe({
      next: () => {
        alert('Organization registered successfully');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        alert('Registration failed');
      }
    });
  }
}
