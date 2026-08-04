import { CurrencyPipe } from '@angular/common';
import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ProdottoDTO } from '../../models/models';
import { generaImmagineProdotto } from '../../utils/immagine-prodotto';
import { API_ORIGIN } from '../../core/api-config';

@Component({
  selector: 'app-prodotto-card',
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './prodotto-card.html',
})
export class ProdottoCard {

  p = input.required<ProdottoDTO>();

  immagineDi(p: ProdottoDTO): string {

    if (p.immagine) return API_ORIGIN + p.immagine;

    const varianti = p.varianti ?? [];
    const neutra = varianti.find((v) => v.immagine && /neutro|natural/i.test(v.gusto ?? ''));
    const variante = neutra ?? varianti.find((v) => v.immagine && v.quantitaDisponibile > 0) ?? varianti.find((v) => v.immagine);
    if (variante?.immagine) return API_ORIGIN + variante.immagine;

    return generaImmagineProdotto(p.nome, p.marca, p.categoria?.nome ?? '', !!p.categoria?.immagine);
  }

  immagineCategoriaDi(p: ProdottoDTO): string | null {
    return p.categoria?.immagine ? API_ORIGIN + p.categoria.immagine : null;
  }

  prezzoMinimo(p: ProdottoDTO): number | null {
    if (!p.varianti?.length) return null;
    return Math.min(...p.varianti.map((v) => v.prezzo));
  }
}
