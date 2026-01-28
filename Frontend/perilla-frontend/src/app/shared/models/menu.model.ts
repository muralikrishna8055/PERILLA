export interface MenuItem {
  label: string;
  icon: string;
  route?: string;
  roles: ('ADMIN' | 'MANAGER' | 'EMPLOYEE')[];
  children?: MenuItem[];
}
