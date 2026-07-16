import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthServices } from '../../auth/auth-services';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private auth = inject(AuthServices);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  erroreMsg = signal<string | null>(null);

  loginForm: FormGroup = new FormGroup({
    email: new FormControl(null, [Validators.required, Validators.email]),
    password: new FormControl(null, Validators.required),
  });

  onSubmit(): void {
    this.erroreMsg.set(null);
    this.auth.login(this.loginForm.value.email, this.loginForm.value.password).subscribe({
      next: () => {
        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || '/catalogo';
        this.router.navigateByUrl(returnUrl);
      },
      error: (err) => {
        this.erroreMsg.set(err.error?.msg ?? 'Errore durante il login');
      },
    });
  }
}
