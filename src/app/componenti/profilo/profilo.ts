import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthServices } from '../../auth/auth-services';
import { IndirizzoServices } from '../../services/indirizzo-services';
import { UtenteServices } from '../../services/utente-services';
import { IndirizzoDTO } from '../../models/models';

@Component({
  selector: 'app-profilo',
  imports: [ReactiveFormsModule],
  templateUrl: './profilo.html',
  styleUrl: './profilo.css',
})
export class Profilo implements OnInit {
  private utenteS = inject(UtenteServices);
  private indirizzoS = inject(IndirizzoServices);
  auth = inject(AuthServices);

  indirizzi = signal<IndirizzoDTO[]>([]);
  messaggioDati = signal<string | null>(null);
  modificaIndirizzoId = signal<number | null>(null);

  datiForm = new FormGroup({
    nome: new FormControl(''),
    cognome: new FormControl(''),
    telefono: new FormControl(''),
  });

  nuovoIndirizzoForm = new FormGroup({
    via: new FormControl('', Validators.required),
    citta: new FormControl('', Validators.required),
    cap: new FormControl('', Validators.required),
    provincia: new FormControl(''),
    nazione: new FormControl('Italia'),
  });

  ngOnInit(): void {
    const utente = this.auth.currentUser();
    if (utente) {
      this.datiForm.patchValue({ nome: utente.nome, cognome: utente.cognome, telefono: utente.telefono });
    }
    this.caricaIndirizzi();
  }

  caricaIndirizzi(): void {
    this.indirizzoS.list().subscribe({
      next: (resp) => this.indirizzi.set(resp),
    });
  }

  salvaDati(): void {
    const utente = this.auth.currentUser();
    if (!utente) return;

    this.utenteS.update({ id: utente.idUtente, ...this.datiForm.value } as any).subscribe({
      next: () => {
        this.messaggioDati.set('Dati aggiornati');
        this.auth.currentUser.set({ ...utente, ...this.datiForm.value } as any);
      },
      error: (err) => this.messaggioDati.set(err.error?.msg ?? 'Errore'),
    });
  }

  aggiungiIndirizzo(): void {
    this.indirizzoS.create(this.nuovoIndirizzoForm.value as any).subscribe({
      next: () => {
        this.nuovoIndirizzoForm.reset({ nazione: 'Italia' });
        this.caricaIndirizzi();
      },
    });
  }

  eliminaIndirizzo(id: number): void {
    this.indirizzoS.delete(id).subscribe({
      next: () => this.caricaIndirizzi(),
    });
  }
}
