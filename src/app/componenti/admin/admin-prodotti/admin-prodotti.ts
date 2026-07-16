import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategoriaServices } from '../../../services/categoria-services';
import { ProdottoServices } from '../../../services/prodotto-services';
import { VarianteServices } from '../../../services/variante-services';
import { CategoriaDTO, ProdottoDTO, VarianteProdottoDTO } from '../../../models/models';

// ============================================================================
// PROPRIETARIO: Mattia — Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
// Il pannello admin piu' complesso del catalogo: gestisce prodotti E le loro varianti
// insieme (tabella espandibile), incrociando 3 service diversi (Categoria/Prodotto/Variante)
@Component({
  selector: 'app-admin-prodotti',
  imports: [ReactiveFormsModule, CurrencyPipe],
  templateUrl: './admin-prodotti.html',
  styleUrl: './admin-prodotti.css',
})
export class AdminProdotti implements OnInit {
  private categoriaS = inject(CategoriaServices);
  private prodottoS = inject(ProdottoServices);
  private varianteS = inject(VarianteServices);

  categorie = signal<CategoriaDTO[]>([]);
  prodotti = signal<ProdottoDTO[]>([]);
  erroreMsg = signal<string | null>(null);
  // Id del prodotto attualmente "aperto" nella tabella (mostra le sue varianti) — null = nessuno espanso
  prodottoEspanso = signal<number | null>(null);

  nuovoProdottoForm = new FormGroup({
    idCategoria: new FormControl<number | null>(null, Validators.required),
    nome: new FormControl('', Validators.required),
    marca: new FormControl('', Validators.required),
    descrizione: new FormControl(''),
  });

  nuovaVarianteForm = new FormGroup({
    gusto: new FormControl(''),
    formato: new FormControl(''),
    colore: new FormControl(''),
    prezzo: new FormControl<number | null>(null, [Validators.required, Validators.min(0)]),
    quantitaDisponibile: new FormControl<number>(0),
  });

  ngOnInit(): void {
    this.categoriaS.list().subscribe({ next: (resp) => this.categorie.set(resp) });
    this.carica();
  }

  carica(): void {
    this.prodottoS.list().subscribe({ next: (resp) => this.prodotti.set(resp) });
  }

  creaProdotto(): void {
    this.erroreMsg.set(null);
    const v = this.nuovoProdottoForm.value;
    if (!v.idCategoria || !v.nome || !v.marca) return;

    this.prodottoS.create({ idCategoria: v.idCategoria, nome: v.nome, marca: v.marca, descrizione: v.descrizione ?? undefined }).subscribe({
      next: () => {
        this.nuovoProdottoForm.reset();
        this.carica();
      },
      // Corrisponde all'errore "prodotto.exists" lanciato da ProdottoImpl.create nel backend
      // se esiste gia' un prodotto con lo stesso nome+marca
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }

  eliminaProdotto(id: number): void {
    this.erroreMsg.set(null);
    this.prodottoS.delete(id).subscribe({
      next: () => this.carica(),
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }

  // Apri/chiudi (toggle): se clicco sul prodotto gia' espanso, lo richiudo
  espandi(idProdotto: number): void {
    this.prodottoEspanso.set(this.prodottoEspanso() === idProdotto ? null : idProdotto);
    this.nuovaVarianteForm.reset({ quantitaDisponibile: 0 });
  }

  // Lo schema non prevede attributi per-categoria: questa mappa e' un'euristica solo
  // di presentazione (nasconde/rinomina i campi gusto/formato/colore in base al nome
  // della categoria) per non mostrare "Gusto" su un capo di abbigliamento, ad esempio.
  attributiCategoria(nomeCategoria?: string): { gusto: boolean; formato: boolean; colore: boolean; labelFormato: string; labelColore: string } {
    const nome = (nomeCategoria ?? '').toLowerCase();

    if (nome.includes('abbiglia')) {
      return { gusto: false, formato: true, colore: true, labelFormato: 'Taglia', labelColore: 'Colore' };
    }
    if (nome.includes('protein') || nome.includes('vitamin') || nome.includes('creatin') || nome.includes('pre') || nome.includes('workout') || nome.includes('integrat')) {
      return { gusto: true, formato: true, colore: false, labelFormato: 'Formato (es. 1kg)', labelColore: 'Colore' };
    }
    return { gusto: true, formato: true, colore: true, labelFormato: 'Formato', labelColore: 'Colore' };
  }

  creaVariante(idProdotto: number): void {
    this.erroreMsg.set(null);
    const v = this.nuovaVarianteForm.value;
    if (v.prezzo === null || v.prezzo === undefined) return;

    this.varianteS.create({
      idProdotto,
      gusto: v.gusto ?? undefined,
      formato: v.formato ?? undefined,
      colore: v.colore ?? undefined,
      prezzo: v.prezzo,
      quantitaDisponibile: v.quantitaDisponibile ?? 0,
    }).subscribe({
      next: () => {
        this.nuovaVarianteForm.reset({ quantitaDisponibile: 0 });
        this.carica();
      },
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }

  eliminaVariante(id: number): void {
    this.erroreMsg.set(null);
    this.varianteS.delete(id).subscribe({
      next: () => this.carica(),
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }

  // Aggiornamento rapido dello stock direttamente dalla tabella, senza aprire un form
  // di modifica completo: chiama VarianteServices.update passando SOLO quantitaDisponibile
  // (aggiornamento parziale, gli altri campi della variante restano invariati)
  aggiornaQuantita(v: VarianteProdottoDTO, quantita: number): void {
    if (quantita < 0) return;
    this.erroreMsg.set(null);
    this.varianteS.update({ id: v.id, quantitaDisponibile: quantita }).subscribe({
      next: () => this.carica(),
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }
}
