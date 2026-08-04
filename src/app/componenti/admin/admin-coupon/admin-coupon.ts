import { CurrencyPipe, DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CouponServices } from '../../../services/coupon-services';
import { CouponDTO } from '../../../models/models';

@Component({
  selector: 'app-admin-coupon',
  imports: [ReactiveFormsModule, CurrencyPipe, DatePipe],
  templateUrl: './admin-coupon.html',
  styleUrl: './admin-coupon.css',
})
export class AdminCoupon implements OnInit {
  private couponS = inject(CouponServices);

  coupon = signal<CouponDTO[]>([]);
  erroreMsg = signal<string | null>(null);

  nuovoForm = new FormGroup({
    codice: new FormControl('', Validators.required),
    tipologia: new FormControl('PERCENTUALE', Validators.required),
    valore: new FormControl<number | null>(null, [Validators.required, Validators.min(0.01)]),
    dataInizio: new FormControl('', Validators.required),
    dataFine: new FormControl('', Validators.required),
  });

  ngOnInit(): void {
    this.carica();
  }

  carica(): void {
    this.couponS.list().subscribe({ next: (resp) => this.coupon.set(resp) });
  }

  crea(): void {
    this.erroreMsg.set(null);
    const v = this.nuovoForm.value;
    if (!v.codice || !v.tipologia || v.valore === null || v.valore === undefined || !v.dataInizio || !v.dataFine) return;

    this.couponS.create({
      codice: v.codice,
      tipologia: v.tipologia,
      valore: v.valore,
      dataInizio: v.dataInizio + 'T00:00:00',
      dataFine: v.dataFine + 'T23:59:59',
    }).subscribe({
      next: () => {
        this.nuovoForm.reset({ tipologia: 'PERCENTUALE' });
        this.carica();
      },

      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }

  attivaDisattiva(c: CouponDTO): void {
    this.couponS.update({ id: c.id, isAttivo: !c.isAttivo }).subscribe({
      next: () => this.carica(),
    });
  }

  elimina(id: number): void {
    this.couponS.delete(id).subscribe({
      next: () => this.carica(),
      error: (err) => this.erroreMsg.set(err.error?.msg ?? 'Errore'),
    });
  }
}
