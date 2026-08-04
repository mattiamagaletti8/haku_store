import { HttpClient } from '@angular/common/http';
import { Injectable, inject, signal } from '@angular/core';
import { tap } from 'rxjs';
import { API_BASE_URL } from '../core/api-config';
import { CarrelloDTO, ResponseDTO } from '../models/models';

@Injectable({ providedIn: 'root' })
export class CarrelloServices {
  private http = inject(HttpClient);
  private url = API_BASE_URL + '/carrello';

  carrello = signal<CarrelloDTO | null>(null);

  numeroArticoli = () =>
    this.carrello()?.righe.reduce((tot, r) => tot + r.quantita, 0) ?? 0;

  ricarica() {
    this.http.get<CarrelloDTO>(this.url).subscribe({
      next: (resp) => this.carrello.set(resp),
    });
  }

  addItem(body: { idVariante: number; quantita: number }) {
    return this.http.post<ResponseDTO>(this.url + '/items', body)
      .pipe(tap(() => this.ricarica()));
  }

  updateItem(body: { idVariante: number; quantita: number }) {
    return this.http.patch<ResponseDTO>(this.url + '/items', body)
      .pipe(tap(() => this.ricarica()));
  }

  removeItem(idVariante: number) {
    return this.http.delete<ResponseDTO>(this.url + '/items/' + idVariante)
      .pipe(tap(() => this.ricarica()));
  }

  applyCoupon(codiceCoupon: string) {
    return this.http.post<ResponseDTO>(this.url + '/coupon', { codiceCoupon })
      .pipe(tap(() => this.ricarica()));
  }

  removeCoupon() {
    return this.http.delete<ResponseDTO>(this.url + '/coupon')
      .pipe(tap(() => this.ricarica()));
  }

  clear() {
    return this.http.delete<ResponseDTO>(this.url + '/clear')
      .pipe(tap(() => this.ricarica()));
  }
}
