export interface AuthToken {
  accessToken: string;
  role: 'ADMIN' | 'MANAGER' | 'EMPLOYEE';
  username: string;
}
