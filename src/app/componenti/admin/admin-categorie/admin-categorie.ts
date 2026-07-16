import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategoriaServices } from '../../../services/categoria-services';
import { CategoriaDTO } from '../../../models/models';

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
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }

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
    this.categoriaS.delete(id).subscribe({
      next: () => this.carica(),
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }
}
