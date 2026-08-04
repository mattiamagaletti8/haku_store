import { Directive, ElementRef, HostListener, Input } from '@angular/core';

@Directive({
  selector: '[appHighLigth1]',
  standalone: true
})
export class HighLigth1 {

  @Input() appHighLigth1 = '';
  @Input() defaultColor = '';

  constructor(private element: ElementRef) {
  }

  @HostListener('mouseenter') onMouseEnter() {
    this.cambioColore(this.appHighLigth1);
  }
  @HostListener('mouseleave') onMouseLeave() {
    this.cambioColore(this.defaultColor);
  }
  private cambioColore(colore: string) {
    this.element.nativeElement.style.backgroundColor = colore;
  }

}
