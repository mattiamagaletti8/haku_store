import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { AuthServices } from '../../auth/auth-services';
import { CarrelloServices } from '../../services/carrello-services';
import { ProdottoServices } from '../../services/prodotto-services';
import { RecensioneServices } from '../../services/recensione-services';
import { ProdottoDTO, RecensioneDTO, VarianteProdottoDTO } from '../../models/models';
import { generaImmagineProdotto } from '../../utils/immagine-prodotto';

@Component({
  selector: 'app-prodotto-dettaglio',
  imports: [ReactiveFormsModule, CurrencyPipe, DatePipe],
  templateUrl: './prodotto-dettaglio.html',
  styleUrl: './prodotto-dettaglio.css',
})
export class ProdottoDettaglio implements OnInit {
  private route = inject(ActivatedRoute);
  private prodottoS = inject(ProdottoServices);
  private recensioneS = inject(RecensioneServices);
  private carrelloS = inject(CarrelloServices);
  auth = inject(AuthServices);

  idProdotto!: number;
  prodotto = signal<ProdottoDTO | null>(null);
  recensioni = signal<RecensioneDTO[]>([]);
  varianteSelezionata = signal<VarianteProdottoDTO | null>(null);
  messaggioCarrello = signal<string | null>(null);

  quantitaForm = new FormGroup({
    quantita: new FormControl(1, [Validators.required, Validators.min(1)]),
  });

  recensioneForm = new FormGroup({
    voto: new FormControl(5, [Validators.required, Validators.min(1), Validators.max(5)]),
    titolo: new FormControl(''),
    commento: new FormControl(''),
  });

  ngOnInit(): void {
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.idProdotto = Number(params.get('id'));
      this.caricaProdotto();
      this.caricaRecensioni();
    });
  }

  caricaProdotto(): void {
    this.prodottoS.getById(this.idProdotto).subscribe({
      next: (resp) => {
        this.prodotto.set(resp);
        const disponibile = resp.varianti.find((v) => v.quantitaDisponibile > 0);
        this.varianteSelezionata.set(disponibile ?? resp.varianti[0] ?? null);
      },
    });
  }

  caricaRecensioni(): void {
    this.recensioneS.listByProdotto(this.idProdotto).subscribe({
      next: (resp) => this.recensioni.set(resp),
    });
  }

  selezionaVariante(v: VarianteProdottoDTO): void {
    if (v.quantitaDisponibile <= 0) return;
    this.varianteSelezionata.set(v);
    this.quantitaForm.patchValue({ quantita: 1 });
  }

  aggiungiAlCarrello(): void {
    const variante = this.varianteSelezionata();
    if (!variante || variante.quantitaDisponibile <= 0) return;

    const quantita = Math.min(this.quantitaForm.value.quantita ?? 1, variante.quantitaDisponibile);

    this.messaggioCarrello.set(null);
    this.carrelloS.addItem({ idVariante: variante.id, quantita }).subscribe({
      next: () => this.messaggioCarrello.set('Aggiunto al carrello!'),
      error: (err) => this.messaggioCarrello.set(err.error?.msg ?? 'Errore'),
    });
  }

  immagineDi(p: ProdottoDTO): string {
    return generaImmagineProdotto(p.nome, p.marca, p.categoria?.nome ?? '');
  }

  inviaRecensione(): void {
    this.recensioneS.create({
      idProdotto: this.idProdotto,
      voto: this.recensioneForm.value.voto ?? 5,
      titolo: this.recensioneForm.value.titolo ?? undefined,
      commento: this.recensioneForm.value.commento ?? undefined,
    }).subscribe({
      next: () => {
        this.recensioneForm.reset({ voto: 5, titolo: '', commento: '' });
        this.caricaRecensioni();
      },
    });
  }
}
