import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/authservice';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  username = '';
  password = '';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private router: Router
  ) {}

  onLogin(): void {
  const payload = {
    username: this.username, // email OR employeeCode
    password: this.password
  };

  this.http.post<any>('http://localhost:8080/api/auth/login', payload)
    .subscribe({
      next: (res) => {
        this.authService.handleLogin(res);

        const role = res.role;
        this.router.navigate(['/dashboard']);
      },
      error: () => alert('Invalid credentials')
    });
}

}
