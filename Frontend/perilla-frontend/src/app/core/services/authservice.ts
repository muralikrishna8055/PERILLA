import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly API_URL = 'http://localhost:8080/api/auth';
  private readonly TOKEN_KEY = 'perilla_token';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  // ================= TOKEN =================
  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  clearSession(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem('role');
    localStorage.removeItem('employeeCode');
    localStorage.removeItem('tenantCode');
  }

  // ================= LOGIN =================
  handleLogin(res: any): void {
    this.setToken(res.token);
  }

  // ================= LOGOUT =================
  logout(): void {
    this.http.post(`${this.API_URL}/logout`, {}).subscribe({
      next: () => this.finishLogout(),
      error: () => this.finishLogout(), // backend failure shouldn't block logout
    });
  }

  private finishLogout(): void {
    this.clearSession();
    this.router.navigate(['/']);
  }

  // ================= AUTH CHECK =================
  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  // ================= JWT =================
  private decodeToken(): any | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const payload = token.split('.')[1];
      return JSON.parse(atob(payload));
    } catch {
      return null;
    }
  }

  // ================= CLAIMS =================
  getRole(): 'ADMIN' | 'MANAGER' | 'EMPLOYEE' | null {
    return this.decodeToken()?.role ?? null;
  }

  getTenantCode(): string | null {
    return this.decodeToken()?.tenantCode ?? null;
  }

  getEmployeeCode(): string | null {
    return this.decodeToken()?.employeeCode ?? null;
  }
}
