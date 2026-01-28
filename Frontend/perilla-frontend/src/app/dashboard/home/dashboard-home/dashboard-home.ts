import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-dashboard-home',
  standalone: true,
  imports: [CommonModule], // ✅ REQUIRED
  templateUrl: './dashboard-home.html',
  styleUrls: ['./dashboard-home.css']
})
export class DashboardHome {

  stats = [
    { label: 'Employees', value: 42, icon: 'bi-people', trend: '+4%' },
    { label: 'Pending Leaves', value: 5, icon: 'bi-calendar-x', trend: '-1%' },
    { label: 'Active Projects', value: 7, icon: 'bi-kanban', trend: '+2%' },
    { label: 'Open Tasks', value: 18, icon: 'bi-list-check', trend: '+6%' }
  ];

 ngAfterViewInit(): void {
    this.loadCharts();
  }

  loadCharts() {
    new Chart('employeeChart', {
      type: 'line',
      data: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
        datasets: [{
          label: 'Employees',
          data: [30, 32, 35, 38, 42],
          borderWidth: 2
        }]
      }
    });

    new Chart('projectChart', {
      type: 'doughnut',
      data: {
        labels: ['Completed', 'In Progress', 'Pending'],
        datasets: [{
          data: [4, 2, 1]
        }]
      }
    });
  }

}

