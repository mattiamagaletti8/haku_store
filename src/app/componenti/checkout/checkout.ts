import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CarrelloServices } from '../../services/carrello-services';
import { IndirizzoServices } from '../../services/indirizzo-services';
import { OrdineServices } from '../../services/ordine-services';
import { IndirizzoDTO } from '../../models/models';

@Component({
  selector: 'app-checkout',
  imports: [ReactiveFormsModule, CurrencyPipe],
  templateUrl: './checkout.html',
  styleUrl: './checkout.css',
})
export class Checkout implements OnInit {
  private indirizzoS = inject(IndirizzoServices);
  private ordineS = inject(OrdineServices);
  private router = inject(Router);
  carrelloS = inject(CarrelloServices);

  indirizzi = signal<IndirizzoDTO[]>([]);
  mostraNuovoIndirizzo = signal(false);
  erroreMsg = signal<string | null>(null);

  checkoutForm = new FormGroup({
    idIndirizzo: new FormControl<number | null>(null, Validators.required),
    metodoPagamento: new FormControl('CARTA', Validators.required),
  });

  nuovoIndirizzoForm = new FormGroup({
    via: new FormControl('', Validators.required),
    citta: new FormControl('', Validators.required),
    cap: new FormControl('', Validators.required),
    provincia: new FormControl(''),
    nazione: new FormControl('Italia'),
  });

  ngOnInit(): void {
    this.carrelloS.ricarica();
    this.caricaIndirizzi();
  }

  caricaIndirizzi(): void {
    this.indirizzoS.list().subscribe({
      next: (resp) => {
        this.indirizzi.set(resp);
        this.mostraNuovoIndirizzo.set(resp.length === 0);
        if (resp.length > 0) this.checkoutForm.patchValue({ idIndirizzo: resp[0].id });
      },
    });
  }

  creaIndirizzo(): void {
    this.indirizzoS.create(this.nuovoIndirizzoForm.value as any).subscribe({
      next: () => {
        this.nuovoIndirizzoForm.reset({ nazione: 'Italia' });
        this.mostraNuovoIndirizzo.set(false);
        this.caricaIndirizzi();
      },
    });
  }

  confermaOrdine(): void {
    this.erroreMsg.set(null);
    const idIndirizzo = this.checkoutForm.value.idIndirizzo;
    const metodoPagamento = this.checkoutForm.value.metodoPagamento;
    if (!idIndirizzo || !metodoPagamento) return;

    this.ordineS.checkout({ idIndirizzo, metodoPagamento }).subscribe({
      next: (ordine) => {
        this.carrelloS.ricarica();
        this.router.navigate(['/ordini', ordine.id]);
      },
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore durante il checkout'),
    });
  }
}
