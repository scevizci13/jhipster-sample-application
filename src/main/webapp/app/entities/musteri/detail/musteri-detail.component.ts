import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IMusteri } from '../musteri.model';

@Component({
  standalone: true,
  selector: 'jhi-musteri-detail',
  templateUrl: './musteri-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MusteriDetailComponent {
  musteri = input<IMusteri | null>(null);

  previousState(): void {
    window.history.back();
  }
}
