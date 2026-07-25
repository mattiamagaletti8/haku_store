import { DatePipe } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthServices } from './auth/auth-services';
import { CarrelloServices } from './services/carrello-services';

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// Il componente RADICE dell'applicazione (teoria cap. 13): tutto il resto vive dentro
// il suo template, nel punto dove c'e' <router-outlet> (vedi app.html). E' l'equivalente
// di app.component.ts nella struttura "classica" descritta in teoria.
@Component({
  selector: 'app-root',
  // RouterOutlet/RouterLink/RouterLinkActive (teoria cap. 21): servono per il routing
  // e per evidenziare il link della pagina attiva nella navbar
  imports: [RouterOutlet, RouterLink, RouterLinkActive, DatePipe],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  // Iniettati qui (nel componente radice) perche' la navbar in app.html ha bisogno
  // di sapere se l'utente e' loggato/admin e quanti articoli ha nel carrello, sempre,
  // su ogni pagina dell'app
  auth = inject(AuthServices);
  carrelloS = inject(CarrelloServices);
  private router = inject(Router);

  annoCorrente = new Date().getFullYear();

  // Saldi in settimane "casuali" dell'anno: la posizione delle settimane e' generata da un
  // seed (l'anno corrente), quindi e' la stessa per tutti i visitatori e non cambia ad ogni
  // refresh della pagina, ma resta comunque imprevedibile senza guardare il codice.
  // Se oggi non cade in nessuna settimana di saldi, la topbar mostra il coupon di benvenuto.
  private readonly saldi = this.calcolaSettimanaSaldi();
  saldiAttivi = this.saldi.attivo;
  fineSaldi = this.saldi.fineSettimana;

  private calcolaSettimanaSaldi(): { attivo: boolean; fineSettimana: Date | null } {
    const oggi = new Date();
    const anno = oggi.getFullYear();
    const giornoDellAnno = Math.floor((oggi.getTime() - new Date(anno, 0, 0).getTime()) / 86400000);

    // generatore pseudo-casuale deterministico (seedato sull'anno): stesse settimane
    // per tutto l'anno, diverse da un anno all'altro
    let seed = anno;
    const random = () => {
      seed = (seed * 1103515245 + 12345) & 0x7fffffff;
      return seed / 0x7fffffff;
    };

    const numeroSettimaneSaldi = 5;
    for (let i = 0; i < numeroSettimaneSaldi; i++) {
      const inizioGiorno = Math.floor(random() * 350) + 1;
      if (giornoDellAnno >= inizioGiorno && giornoDellAnno < inizioGiorno + 7) {
        return { attivo: true, fineSettimana: new Date(anno, 0, inizioGiorno + 7) };
      }
    }
    return { attivo: false, fineSettimana: null };
  }

  ngOnInit(): void {
    // Se l'utente arriva gia' loggato (sessione salvata in localStorage, vedi AuthServices),
    // precarica subito il carrello cosi' il badge nella navbar e' corretto fin da subito
    if (this.auth.isLogged()) this.carrelloS.ricarica();
  }

  logout(): void {
    this.auth.logout();
    this.carrelloS.carrello.set(null);
    this.router.navigate(['/login']);
  }
}
