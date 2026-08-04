import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthServices } from './auth/auth-services';
import { CarrelloServices } from './services/carrello-services';

@Component({
  selector: 'app-root',

  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {

  auth = inject(AuthServices);
  carrelloS = inject(CarrelloServices);
  private router = inject(Router);

  annoCorrente = new Date().getFullYear();

  ngOnInit(): void {

    if (this.auth.isLogged()) this.carrelloS.ricarica();
  }

  logout(): void {
    this.auth.logout();
    this.carrelloS.carrello.set(null);
    this.router.navigate(['/login']);
  }
}
