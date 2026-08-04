import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategoriaServices } from '../../../services/categoria-services';
import { UploadServices } from '../../../services/upload-services';
import { CategoriaDTO } from '../../../models/models';
import { API_ORIGIN } from '../../../core/api-config';

@Component({
  selector: 'app-admin-categorie',
  imports: [ReactiveFormsModule],
  templateUrl: './admin-categorie.html',
  styleUrl: './admin-categorie.css',
})
export class AdminCategorie implements OnInit {
  private categoriaS = inject(CategoriaServices);
  private uploadS = inject(UploadServices);

  readonly apiOrigin = API_ORIGIN;

  categorie = signal<CategoriaDTO[]>([]);
  erroreMsg = signal<string | null>(null);

  inModifica = signal<number | null>(null);

  caricamentoImmagine = signal<number | null>(null);

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

  onFileSelected(event: Event, categoria: CategoriaDTO): void {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    this.caricaImmagine(file, categoria);

    input.value = '';
  }

  private caricaImmagine(file: File, categoria: CategoriaDTO): void {
    this.erroreMsg.set(null);
    this.caricamentoImmagine.set(categoria.id);

    this.uploadS.uploadImage(file, categoria.id).subscribe({
      next: (r) => {
        this.uploadS.getUrl(r.msg).subscribe({
          next: (r2) => {
            categoria.immagine = r2.msg;
            this.caricamentoImmagine.set(null);
          },
          error: (err) => {
            this.erroreMsg.set(err.error?.msg ?? 'Errore');
            this.caricamentoImmagine.set(null);
          },
        });
      },
      error: (err) => {
        this.erroreMsg.set(err.error?.msg ?? 'Errore');
        this.caricamentoImmagine.set(null);
      },
    });
  }
}
