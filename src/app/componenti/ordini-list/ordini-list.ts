import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { OrdineServices } from '../../services/ordine-services';
import { OrdineDTO } from '../../models/models';

@Component({
  selector: 'app-ordini-list',
  imports: [RouterLink, CurrencyPipe, DatePipe],
  templateUrl: './ordini-list.html',
  styleUrl: './ordini-list.css',
})
export class OrdiniList implements OnInit {
  private ordineS = inject(OrdineServices);

  ordini = signal<OrdineDTO[]>([]);

  ngOnInit(): void {
    this.ordineS.list().subscribe({
      next: (resp) => this.ordini.set(resp),
    });
  }
}
