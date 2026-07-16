import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthServices } from '../../auth/auth-services';

@Component({
  selector: 'app-registrazione',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './registrazione.html',
  styleUrl: './registrazione.css',
})
export class Registrazione {
  private auth = inject(AuthServices);
  private router = inject(Router);

  erroreMsg = signal<string | null>(null);

  registraForm: FormGroup = new FormGroup({
    nome: new FormControl(null, Validators.required),
    cognome: new FormControl(null, Validators.required),
    email: new FormControl(null, [Validators.required, Validators.email]),
    password: new FormControl(null, [Validators.required, Validators.minLength(8)]),
    telefono: new FormControl(null),
  });

  onSubmit(): void {
    this.erroreMsg.set(null);
    this.auth.register(this.registraForm.value).subscribe({
      next: () => this.router.navigateByUrl('/catalogo'),
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore durante la registrazione'),
    });
  }
}
