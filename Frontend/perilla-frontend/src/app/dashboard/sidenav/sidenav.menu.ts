import { MenuItem } from '../../shared/models/menu.model';

export const MENU: MenuItem[] = [

  /* ================= DASHBOARD ================= */
  {
    label: 'Dashboard',
    icon: 'bi-speedometer2',
    route: '/dashboard/home',
    roles: ['ADMIN', 'MANAGER', 'EMPLOYEE']
  },

  /* ================= EMPLOYEE MANAGEMENT ================= */
  {
    label: 'Employee Management',
    icon: 'bi-people',
    roles: ['ADMIN', 'MANAGER'],
    children: [
      {
        label: 'Employee List',
        icon: 'bi-list-ul',
        route: '/employee/list',
        roles: ['ADMIN', 'MANAGER']
      },
      {
        label: 'Add Employee',
        icon: 'bi-person-plus',
        route: '/employee/add',
        roles: ['ADMIN']
      },
      {
        label: 'Attendance',
        icon: 'bi-calendar-check',
        route: '/employee/attendance',
        roles: ['ADMIN', 'MANAGER']
      },
      {
        label: 'Payroll',
        icon: 'bi-cash-stack',
        route: '/employee/payroll',
        roles: ['ADMIN']
      }
    ]
  },

  /* ================= DEPARTMENT MANAGEMENT ================= */
  {
    label: 'Department Management',
    icon: 'bi-diagram-3',
    roles: ['ADMIN'],
    children: [
      {
        label: 'Departments',
        icon: 'bi-building',
        route: '/department/list',
        roles: ['ADMIN']
      }
    ]
  },

  /* ================= PROJECT MANAGEMENT ================= */
  {
    label: 'Project Management',
    icon: 'bi-kanban',
    roles: ['ADMIN', 'MANAGER'],
    children: [
      {
        label: 'Projects',
        icon: 'bi-folder2-open',
        route: '/project/list',
        roles: ['ADMIN', 'MANAGER']
      },
      {
        label: 'Tasks',
        icon: 'bi-check2-square',
        route: '/task/list',
        roles: ['ADMIN', 'MANAGER']
      },
      {
        label: 'Tickets',
        icon: 'bi-life-preserver',
        route: '/ticket/list',
        roles: ['ADMIN', 'MANAGER']
      }
    ]
  },

  /* ================= INVENTORY MANAGEMENT ================= */
  {
    label: 'Inventory Management',
    icon: 'bi-box-seam',
    roles: ['ADMIN'],
    children: [
      {
        label: 'Inventory Items',
        icon: 'bi-box',
        route: '/inventory/list',
        roles: ['ADMIN']
      }
    ]
  },

  /* ================= ASSET MANAGEMENT ================= */
  {
    label: 'Asset Management',
    icon: 'bi-pc-display',
    roles: ['ADMIN'],
    children: [
      {
        label: 'Assets',
        icon: 'bi-laptop',
        route: '/asset/list',
        roles: ['ADMIN']
      },
      {
        label: 'Asset Assignment',
        icon: 'bi-arrow-left-right',
        route: '/asset/assign',
        roles: ['ADMIN']
      }
    ]
  },

  /* ================= SOFTWARE MANAGEMENT ================= */
  {
    label: 'Software Management',
    icon: 'bi-hdd-network',
    roles: ['ADMIN'],
    children: [
      {
        label: 'Software Catalog',
        icon: 'bi-collection',
        route: '/software/list',
        roles: ['ADMIN']
      },
      {
        label: 'License Tracking',
        icon: 'bi-key',
        route: '/software/licenses',
        roles: ['ADMIN']
      }
    ]
  }

];
