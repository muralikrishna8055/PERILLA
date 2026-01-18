import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

constructor(private router: Router) {}

  register() {
    alert('Company Registered Successfully (Mock)');
    this.router.navigate(['/login']);
  }

}
