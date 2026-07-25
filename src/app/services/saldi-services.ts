import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { API_BASE_URL } from '../core/api-config';
import { SaldiStatoDTO } from '../models/models';

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// Le settimane di saldi sono calcolate SOLO nel backend (stessa logica usata per scontare
// davvero i prezzi in carrello/checkout): il frontend si limita a chiedere lo stato corrente,
// cosi' la topbar e' sempre coerente con lo sconto realmente applicato.
@Injectable({ providedIn: 'root' })
export class SaldiServices {
  private http = inject(HttpClient);
  private url = API_BASE_URL + '/saldi/';

  stato() {
    return this.http.get<SaldiStatoDTO>(this.url + 'stato');
  }
}
