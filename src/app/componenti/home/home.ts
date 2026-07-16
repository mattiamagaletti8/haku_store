import { Component, OnInit, inject, signal } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { ProdottoServices } from '../../services/prodotto-services';
import { CategoriaServices } from '../../services/categoria-services';
import { CategoriaDTO, ProdottoDTO } from '../../models/models';
import { generaImmagineProdotto } from '../../utils/immagine-prodotto';

// ============================================================================
// PROPRIETARIO: Mattia — Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
// Home/catalogo: la pagina pubblica principale, nessun guard, visibile anche senza login
@Component({
  selector: 'app-home',
  // CurrencyPipe (teoria cap. 17, Pipes native) importato esplicitamente perche'
  // e' un componente standalone: ogni pipe/direttiva usata nel template va dichiarata qui
  imports: [ReactiveFormsModule, RouterLink, CurrencyPipe],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private prodottoS = inject(ProdottoServices);
  private categoriaS = inject(CategoriaServices);

  prodotti = signal<ProdottoDTO[]>([]);
  categorie = signal<CategoriaDTO[]>([]);

  filtriForm: FormGroup = new FormGroup({
    nome: new FormControl(null),
    marca: new FormControl(null),
    idCategoria: new FormControl<string>(''),
    ordinePrezzo: new FormControl<'' | 'asc' | 'desc'>(''),
  });

  ngOnInit(): void {
    this.categoriaS.list().subscribe({ next: (resp) => this.categorie.set(resp) });
    this.carica();
  }

  // Chiamata sia al primo caricamento sia ogni volta che i filtri cambiano
  carica(): void {
    const { nome, marca, idCategoria, ordinePrezzo } = this.filtriForm.value;
    const idCategoriaNum = idCategoria ? Number(idCategoria) : undefined;
    // La ricerca per nome/marca/categoria e' delegata al backend (query nominata
    // prodotto.selectByFilter): il frontend passa solo i filtri effettivamente impostati
    this.prodottoS.list({ nome, marca, idCategoria: idCategoriaNum }).subscribe({
      next: (resp) => this.prodotti.set(this.ordina(resp, ordinePrezzo)),
    });
  }

  // L'ordinamento per prezzo invece avviene qui lato client: il backend non lo supporta
  // direttamente, quindi si riordina l'array gia' ricevuto
  private ordina(lista: ProdottoDTO[], ordinePrezzo: string): ProdottoDTO[] {
    if (!ordinePrezzo) return lista;
    const conPrezzo = (p: ProdottoDTO) => this.prezzoMinimo(p) ?? 0;
    const copia = [...lista];
    copia.sort((a, b) => (ordinePrezzo === 'asc' ? conPrezzo(a) - conPrezzo(b) : conPrezzo(b) - conPrezzo(a)));
    return copia;
  }

  // Un prodotto puo' avere piu' varianti (gusti/formati) con prezzi diversi:
  // in lista si mostra sempre "da [prezzo piu' basso]"
  prezzoMinimo(p: ProdottoDTO): number | null {
    if (!p.varianti?.length) return null;
    return Math.min(...p.varianti.map((v) => v.prezzo));
  }

  immagineDi(p: ProdottoDTO): string {
    return generaImmagineProdotto(p.nome, p.marca, p.categoria?.nome ?? '');
  }
}
