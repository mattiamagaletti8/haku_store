import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CarrelloServices } from '../../services/carrello-services';
import { DettaglioCarrelloDTO } from '../../models/models';

@Component({
  selector: 'app-carrello',
  imports: [ReactiveFormsModule, RouterLink, CurrencyPipe],
  templateUrl: './carrello.html',
  styleUrl: './carrello.css',
})
export class Carrello implements OnInit {

  carrelloS = inject(CarrelloServices);

  erroreCoupon = signal<string | null>(null);

  couponForm = new FormGroup({
    codice: new FormControl(''),
  });

  ngOnInit(): void {
    this.carrelloS.ricarica();
  }

  aggiornaQuantita(riga: DettaglioCarrelloDTO, quantita: number): void {
    if (quantita < 1) return;

    this.carrelloS.updateItem({ idVariante: riga.variante.id, quantita }).subscribe();
  }

  rimuovi(riga: DettaglioCarrelloDTO): void {
    this.carrelloS.removeItem(riga.variante.id).subscribe();
  }

  applicaCoupon(): void {
    this.erroreCoupon.set(null);
    const codice = this.couponForm.value.codice;
    if (!codice) return;

    this.carrelloS.applyCoupon(codice).subscribe({
      next: () => this.couponForm.reset(),
      error: (err) => this.erroreCoupon.set(err.error?.msg ?? 'Coupon non valido'),
    });
  }

  rimuoviCoupon(): void {
    this.carrelloS.removeCoupon().subscribe();
  }
}
