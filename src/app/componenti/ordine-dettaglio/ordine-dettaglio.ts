import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { OrdineServices } from '../../services/ordine-services';
import { OrdineDTO } from '../../models/models';

@Component({
  selector: 'app-ordine-dettaglio',
  imports: [CurrencyPipe, DatePipe],
  templateUrl: './ordine-dettaglio.html',
  styleUrl: './ordine-dettaglio.css',
})
export class OrdineDettaglio implements OnInit {
  private route = inject(ActivatedRoute);
  private ordineS = inject(OrdineServices);

  ordine = signal<OrdineDTO | null>(null);

  ngOnInit(): void {

    this.route.paramMap.subscribe((params: ParamMap) => {
      const id = Number(params.get('id'));
      this.ordineS.getById(id).subscribe({
        next: (resp) => this.ordine.set(resp),
      });
    });
  }
}
