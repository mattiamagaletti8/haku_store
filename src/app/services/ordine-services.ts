import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { API_BASE_URL } from '../core/api-config';
import { OrdineDTO, ResponseDTO } from '../models/models';

@Injectable({ providedIn: 'root' })
export class OrdineServices {
  private http = inject(HttpClient);
  private url = API_BASE_URL + '/ordine/';

  checkout(body: { idIndirizzo: number; metodoPagamento: string }) {
    return this.http.post<OrdineDTO>(this.url + 'checkout', body);
  }

  list(filtri?: { idUtente?: number; stato?: string; statoPagamento?: string }) {
    const params: any = {};
    if (filtri?.idUtente) params.idUtente = filtri.idUtente;
    if (filtri?.stato) params.stato = filtri.stato;
    if (filtri?.statoPagamento) params.statoPagamento = filtri.statoPagamento;
    return this.http.get<OrdineDTO[]>(this.url + 'list', { params });
  }

  getById(id: number) {
    return this.http.get<OrdineDTO>(this.url + 'getById', { params: { id } });
  }

  updateStato(body: { id: number; stato: string }) {
    return this.http.patch<ResponseDTO>(this.url + 'updateStato', body);
  }

  updateStatoPagamento(body: { id: number; statoPagamento: string }) {
    return this.http.patch<ResponseDTO>(this.url + 'updateStatoPagamento', body);
  }
}
