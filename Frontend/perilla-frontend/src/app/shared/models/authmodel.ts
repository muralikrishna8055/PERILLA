export interface LoginResponse {
  token: string;
  role: 'ADMIN' | 'MANAGER' | 'EMPLOYEE';
  tenantCode: string;
  employeeCode?: string;
}

