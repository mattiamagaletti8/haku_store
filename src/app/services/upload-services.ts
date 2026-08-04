import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { API_ORIGIN } from '../core/api-config';
import { ResponseDTO } from '../models/models';

export type TipoUpload = 'categoria' | 'variante';

@Injectable({ providedIn: 'root' })
export class UploadServices {
  private http = inject(HttpClient);
  private url = API_ORIGIN + '/upload/admin/';

  uploadImage(file: File, id: number, tipo: TipoUpload = 'categoria') {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('id', String(id));
    formData.append('tipo', tipo);

    return this.http.post<ResponseDTO>(this.url + 'image', formData);
  }

  getUrl(filename: string, tipo: TipoUpload = 'categoria') {
    const params = new HttpParams().set('filename', filename).set('tipo', tipo);
    return this.http.get<ResponseDTO>(this.url + 'getUrl', { params });
  }
}
