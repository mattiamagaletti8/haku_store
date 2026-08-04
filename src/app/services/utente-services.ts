import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { API_BASE_URL } from '../core/api-config';
import { ResponseDTO, UtenteDTO } from '../models/models';

@Injectable({ providedIn: 'root' })
export class UtenteServices {
  private http = inject(HttpClient);
  private url = API_BASE_URL + '/utente/';

  me() {
    return this.http.get<UtenteDTO>(this.url + 'me');
  }

  getById(id: number) {
    return this.http.get<UtenteDTO>(this.url + 'getById', { params: { id } });
  }

  list() {
    return this.http.get<UtenteDTO[]>(this.url + 'list');
  }

  update(body: { id: number; nome?: string; cognome?: string; email?: string; password?: string; telefono?: string }) {
    return this.http.patch<ResponseDTO>(this.url + 'update', body);
  }

  delete(id: number) {
    return this.http.delete<ResponseDTO>(this.url + 'delete/' + id);
  }
}
