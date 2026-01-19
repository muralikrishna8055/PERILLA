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
      username: this.username,
      password: this.password
    };

    this.http.post<any>('http://localhost:8081/api/auth/login', payload)
      .subscribe({
        next: (res) => {
          this.authService.setToken(res.token);

          const role = this.authService.getRole();
          if (role === 'ADMIN' || role === 'MANAGER') {
  this.router.navigate(['/dashboard']);
} else {
  this.router.navigate(['/dashboard']);
}

        },
        error: () => alert('Invalid credentials')
      });
  }
}
