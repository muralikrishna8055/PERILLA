import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private TOKEN_KEY = 'perilla_token';

  // 🔐 Save JWT
  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  // 🔐 Get JWT
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  // ✅ Auth check
  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  // 🧠 Decode JWT payload
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

  // 🎭 Get role from JWT
  getRole(): string | null {
    const decoded = this.decodeToken();
    return decoded?.role || decoded?.roles || null;
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  // 🔐 TEMP login (replace with HTTP call)
  login(token: string): void {
    this.setToken(token);
  }
}
