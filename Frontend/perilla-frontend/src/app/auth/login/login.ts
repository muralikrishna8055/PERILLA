import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/authservice';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  email = '';
  password = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onLogin(): void {
    if (!this.email) {
      alert('Enter email');
      return;
    }

    this.authService.login(this.email);
    this.router.navigate(['/dashboard']);
  }
}


