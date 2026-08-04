import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, ParamMap, RouterLink } from '@angular/router';
import { AuthServices } from '../../auth/auth-services';
import { CarrelloServices } from '../../services/carrello-services';
import { ProdottoServices } from '../../services/prodotto-services';
import { RecensioneServices } from '../../services/recensione-services';
import { ProdottoDTO, RecensioneDTO, VarianteProdottoDTO } from '../../models/models';
import { generaImmagineProdotto } from '../../utils/immagine-prodotto';
import { API_ORIGIN } from '../../core/api-config';

@Component({
  selector: 'app-prodotto-dettaglio',
  imports: [ReactiveFormsModule, CurrencyPipe, DatePipe, RouterLink],
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

  mediaVoto = computed(() => {
    const r = this.recensioni();
    if (r.length === 0) return null;
    return { media: r.reduce((acc, x) => acc + x.voto, 0) / r.length, count: r.length };
  });

  round = Math.round;

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
    this.varianteSelezionata.set(v);
    this.quantitaForm.patchValue({ quantita: 1 });
  }

  gruppiVarianti = computed(() => {
    const p = this.prodotto();
    if (!p) return [];

    const mappa = new Map<string, VarianteProdottoDTO[]>();
    for (const v of p.varianti) {
      const chiave = `${v.gusto ?? ''}|${v.colore ?? ''}`;
      if (!mappa.has(chiave)) mappa.set(chiave, []);
      mappa.get(chiave)!.push(v);
    }

    return Array.from(mappa.values()).map((varianti) => ({
      gusto: varianti[0].gusto,
      colore: varianti[0].colore,
      varianti,
    }));
  });

  formatoAttivo(g: { varianti: VarianteProdottoDTO[] }): VarianteProdottoDTO {
    const sel = this.varianteSelezionata();
    if (sel && g.varianti.some((v) => v.id === sel.id)) return sel;
    return g.varianti.find((v) => v.quantitaDisponibile > 0) ?? g.varianti[0];
  }

  tutteEsaurite(g: { varianti: VarianteProdottoDTO[] }): boolean {
    return g.varianti.every((v) => v.quantitaDisponibile <= 0);
  }

  selezionaFormato(g: { varianti: VarianteProdottoDTO[] }, idVariante: number): void {
    const v = g.varianti.find((x) => x.id === idVariante);
    if (v) this.selezionaVariante(v);
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

    const variante = this.varianteSelezionata();
    if (variante?.immagine) return API_ORIGIN + variante.immagine;

    if (p.immagine) return API_ORIGIN + p.immagine;

    return generaImmagineProdotto(p.nome, p.marca, p.categoria?.nome ?? '', !!p.categoria?.immagine);
  }

  immagineCategoriaDi(p: ProdottoDTO): string | null {
    return p.categoria?.immagine ? API_ORIGIN + p.categoria.immagine : null;
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
