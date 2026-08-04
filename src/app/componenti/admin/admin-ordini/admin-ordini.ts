import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { OrdineServices } from '../../../services/ordine-services';
import { OrdineDTO } from '../../../models/models';

@Component({
  selector: 'app-admin-ordini',
  imports: [CurrencyPipe, DatePipe],
  templateUrl: './admin-ordini.html',
  styleUrl: './admin-ordini.css',
})
export class AdminOrdini implements OnInit {
  private ordineS = inject(OrdineServices);

  ordini = signal<OrdineDTO[]>([]);

  statiOrdine = ['IN_ATTESA', 'ELABORATO', 'SPEDITO', 'ANNULLATO'];
  statiPagamento = ['DA_PAGARE', 'APPROVATO', 'FALLITO'];

  ngOnInit(): void {
    this.carica();
  }

  carica(): void {
    this.ordineS.list().subscribe({ next: (resp) => this.ordini.set(resp) });
  }

  cambiaStato(id: number, stato: string): void {
    this.ordineS.updateStato({ id, stato }).subscribe({ next: () => this.carica() });
  }

  cambiaStatoPagamento(id: number, statoPagamento: string): void {
    this.ordineS.updateStatoPagamento({ id, statoPagamento }).subscribe({ next: () => this.carica() });
  }
}
