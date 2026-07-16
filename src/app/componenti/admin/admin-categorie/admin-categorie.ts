import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategoriaServices } from '../../../services/categoria-services';
import { CategoriaDTO } from '../../../models/models';

// ============================================================================
// PROPRIETARIO: Mattia — Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
// Pannello admin CRUD per le categorie. Raggiungibile solo passando dal adminGuardGuard
// (definito in app.routes.ts sulla rotta "admin"), quindi solo un utente ADMIN arriva qui.
@Component({
  selector: 'app-admin-categorie',
  imports: [ReactiveFormsModule],
  templateUrl: './admin-categorie.html',
  styleUrl: './admin-categorie.css',
})
export class AdminCategorie implements OnInit {
  private categoriaS = inject(CategoriaServices);

  categorie = signal<CategoriaDTO[]>([]);
  erroreMsg = signal<string | null>(null);
  // Tiene l'id della categoria in modifica inline nella tabella (null = nessuna riga in editing)
  inModifica = signal<number | null>(null);

  nuovaForm = new FormGroup({
    nome: new FormControl('', Validators.required),
  });

  modificaForm = new FormGroup({
    nome: new FormControl('', Validators.required),
  });

  ngOnInit(): void {
    this.carica();
  }

  carica(): void {
    this.categoriaS.list().subscribe({ next: (resp) => this.categorie.set(resp) });
  }

  crea(): void {
    this.erroreMsg.set(null);
    const nome = this.nuovaForm.value.nome;
    if (!nome) return;

    this.categoriaS.create({ nome }).subscribe({
      next: () => {
        this.nuovaForm.reset();
        this.carica();
      },
      // err.error?.msg legge il ResponseDTO{msg} restituito da ExceptionManager nel backend
      // (es. "categoria.nome.exist" gia' tradotto in italiano da IMessaggioServices)
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }

  // Precompila il form di modifica con i dati attuali e attiva la modalita' editing
  // inline per quella specifica riga della tabella
  iniziaModifica(c: CategoriaDTO): void {
    this.inModifica.set(c.id);
    this.modificaForm.setValue({ nome: c.nome });
  }

  salvaModifica(id: number): void {
    this.erroreMsg.set(null);
    const nome = this.modificaForm.value.nome;
    if (!nome) return;

    this.categoriaS.update({ id, nome }).subscribe({
      next: () => {
        this.inModifica.set(null);
        this.carica();
      },
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }

  elimina(id: number): void {
    this.erroreMsg.set(null);
    // Se la categoria ha ancora prodotti collegati, il backend risponde con l'errore
    // "categoria.has.prodotti" invece di cancellare (vedi CategoriaImpl.delete)
    this.categoriaS.delete(id).subscribe({
      next: () => this.carica(),
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }
}
