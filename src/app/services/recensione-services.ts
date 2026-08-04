import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { API_BASE_URL } from '../core/api-config';
import { RecensioneDTO, ResponseDTO } from '../models/models';

@Injectable({ providedIn: 'root' })
export class RecensioneServices {
  private http = inject(HttpClient);
  private url = API_BASE_URL + '/recensione/';

  listByProdotto(idProdotto: number) {
    return this.http.get<RecensioneDTO[]>(this.url + 'list', { params: { idProdotto } });
  }

  create(body: { idProdotto: number; voto: number; titolo?: string; commento?: string }) {
    return this.http.post<ResponseDTO>(this.url + 'create', body);
  }

  update(body: { id: number; voto?: number; titolo?: string; commento?: string }) {
    return this.http.patch<ResponseDTO>(this.url + 'update', body);
  }

  delete(id: number) {
    return this.http.delete<ResponseDTO>(this.url + 'delete/' + id);
  }
}
