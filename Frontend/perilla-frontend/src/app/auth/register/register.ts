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

  private readonly API_URL = 'http://localhost:8080/api/auth/register';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  onRegister(): void {

    const payload = {
      organizationName: this.organizationName.trim(),
      tenantCode: this.tenantCode.trim().toUpperCase(),
      adminUsername: this.adminUsername.trim(),
      password: this.password
    };

    this.http.post<{ message: string }>(this.API_URL, payload)
      .subscribe({
        next: (res) => {
          alert(res.message);
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.error(err);
          alert(err?.error?.message || 'Registration failed');
        }
      });
  }
}
