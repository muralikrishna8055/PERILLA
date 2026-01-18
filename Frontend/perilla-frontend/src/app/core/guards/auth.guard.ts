import { CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/authservice';

export const authGuard: CanActivateFn = (
  route: ActivatedRouteSnapshot,
  state: RouterStateSnapshot
) => {

  const auth = inject(AuthService);
  const router = inject(Router);

  // 1️⃣ Not logged in
  if (!auth.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  // 2️⃣ Role-based access
  const allowedRoles = route.data?.['roles'] as string[] | undefined;
  const userRole = auth.getRole();

  if (allowedRoles && (!userRole || !allowedRoles.includes(userRole))) {
    router.navigate(['/dashboard']);
    return false;
  }

  return true;
};