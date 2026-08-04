import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Injectable, PLATFORM_ID, computed, inject, signal } from '@angular/core';
import { tap } from 'rxjs';
import { API_BASE_URL } from '../core/api-config';
import { AuthResponseDTO, UtenteDTO } from '../models/models';

const TOKEN_KEY = 'hakustore_token';
const TOKEN_EXP_KEY = 'hakustore_token_exp';
const USER_KEY = 'hakustore_user';

@Injectable({ providedIn: 'root' })
export class AuthServices {

  private http = inject(HttpClient);
  private url = API_BASE_URL + '/auth/';

  private isBrowser = isPlatformBrowser(inject(PLATFORM_ID));

  currentUser = signal<UtenteDTO | null>(this.leggiUtenteSalvato());

  isLogged = computed(() => this.currentUser() !== null && !this.tokenScaduto());
  isAdmin = computed(() => this.currentUser()?.ruolo === 'ADMIN');

  login(email: string, password: string) {
    return this.http.post<AuthResponseDTO>(this.url + 'login', { email, password })

      .pipe(tap((resp) => this.salvaSessione(resp)));
  }

  register(body: { nome: string; cognome: string; email: string; password: string; telefono?: string }) {
    return this.http.post<AuthResponseDTO>(this.url + 'register', body)
      .pipe(tap((resp) => this.salvaSessione(resp)));
  }

  logout(): void {
    if (this.isBrowser) {
      localStorage.removeItem(TOKEN_KEY);
      localStorage.removeItem(TOKEN_EXP_KEY);
      localStorage.removeItem(USER_KEY);
    }
    this.currentUser.set(null);
  }

  getToken(): string | null {
    if (!this.isBrowser || this.tokenScaduto()) return null;
    return localStorage.getItem(TOKEN_KEY);
  }

  private salvaSessione(resp: AuthResponseDTO): void {
    if (this.isBrowser) {
      const scadenza = Date.now() + resp.expiresIn * 1000;
      localStorage.setItem(TOKEN_KEY, resp.token);
      localStorage.setItem(TOKEN_EXP_KEY, String(scadenza));
      localStorage.setItem(USER_KEY, JSON.stringify(resp.utente));
    }
    this.currentUser.set(resp.utente);
  }

  private tokenScaduto(): boolean {
    if (!this.isBrowser) return true;
    const scadenza = localStorage.getItem(TOKEN_EXP_KEY);
    if (!scadenza) return true;
    return Date.now() > Number(scadenza);
  }

  private leggiUtenteSalvato(): UtenteDTO | null {
    if (!this.isBrowser) return null;
    const raw = localStorage.getItem(USER_KEY);
    if (!raw) return null;
    try {
      return JSON.parse(raw) as UtenteDTO;
    } catch {
      return null;
    }
  }
}
