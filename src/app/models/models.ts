// Interfacce TypeScript che rispecchiano i DTO del backend hakustore (stessi nomi di campo,
// perche' Jackson serializza i field Java cosi' come sono, in camelCase).

export interface UtenteDTO {
  idUtente: number;
  nome: string;
  cognome: string;
  email: string;
  telefono?: string;
  ruolo: 'CLIENTE' | 'ADMIN';
}

export interface AuthResponseDTO {
  token: string;
  tokenType: string;
  expiresIn: number;
  utente: UtenteDTO;
}

export interface ResponseDTO {
  msg: string;
}

export interface CategoriaDTO {
  id: number;
  nome: string;
}

export interface VarianteProdottoDTO {
  id: number;
  idProdotto: number;
  nomeProdotto?: string;
  gusto?: string;
  formato?: string;
  colore?: string;
  prezzo: number;
  quantitaDisponibile: number;
}

export interface ProdottoDTO {
  id: number;
  nome: string;
  descrizione?: string;
  marca: string;
  categoria: CategoriaDTO;
  varianti: VarianteProdottoDTO[];
}

export interface RecensioneDTO {
  id: number;
  idProdotto: number;
  idUtente: number;
  nomeUtente: string;
  cognomeUtente: string;
  voto: number;
  titolo?: string;
  commento?: string;
  dataRecensione: string;
}

export interface CouponDTO {
  id: number;
  codice: string;
  tipologia: 'PERCENTUALE' | 'FISSO';
  valore: number;
  dataInizio: string;
  dataFine: string;
  isAttivo: boolean;
}

export interface DettaglioCarrelloDTO {
  id: number;
  variante: VarianteProdottoDTO;
  quantita: number;
  subtotale: number;
}

export interface CarrelloDTO {
  id: number;
  dataCreazione: string;
  righe: DettaglioCarrelloDTO[];
  coupon?: CouponDTO;
  totaleProdotti: number;
  valoreSconto: number;
  totalePagato: number;
}

export interface IndirizzoDTO {
  id: number;
  via: string;
  citta: string;
  cap: string;
  provincia?: string;
  nazione: string;
}

export interface DettaglioOrdineDTO {
  id: number;
  variante: VarianteProdottoDTO;
  quantita: number;
  prezzoUnitario: number;
  subtotale: number;
}

export interface OrdineDTO {
  id: number;
  idUtente: number;
  dataOrdine: string;
  totaleProdotti: number;
  valoreSconto: number;
  totalePagato: number;
  codiceCouponUsato?: string;
  stato: 'IN_ATTESA' | 'ELABORATO' | 'SPEDITO' | 'ANNULLATO';
  spedizioneVia: string;
  spedizioneCitta: string;
  spedizioneCap: string;
  spedizioneProvincia?: string;
  spedizioneNazione: string;
  metodoPagamento: string;
  statoPagamento: 'DA_PAGARE' | 'APPROVATO' | 'FALLITO';
  righe: DettaglioOrdineDTO[];
}
