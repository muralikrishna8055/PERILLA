import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { AuthToken } from '../../shared/models/authmodel';

@Injectable({ providedIn: 'root' })
export class AuthService {

  private TOKEN_KEY = 'perilla_auth';

  private authState = new BehaviorSubject<boolean>(this.hasToken());
  auth$ = this.authState.asObservable();

  constructor(private router: Router) {}

  private hasToken(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }

  login(email: string): void {
    let role: 'ADMIN' | 'MANAGER' | 'EMPLOYEE' = 'EMPLOYEE';

    if (email.includes('admin')) role = 'ADMIN';
    else if (email.includes('manager')) role = 'MANAGER';

    const mockToken: AuthToken = {
      accessToken: 'mock-jwt-token-xyz',
      role,
      username: email
    };

    localStorage.setItem(this.TOKEN_KEY, JSON.stringify(mockToken));
    this.authState.next(true);
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.authState.next(false);
    this.router.navigate(['/login']);
  }

  getToken(): AuthToken | null {
    const token = localStorage.getItem(this.TOKEN_KEY);
    return token ? JSON.parse(token) : null;
  }

  isAuthenticated(): boolean {
    return this.authState.value;
  }

  getRole(): string | null {
    return this.getToken()?.role || null;
  }
}
