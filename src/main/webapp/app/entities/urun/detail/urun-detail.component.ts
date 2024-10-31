import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUrun } from '../urun.model';

@Component({
  standalone: true,
  selector: 'jhi-urun-detail',
  templateUrl: './urun-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UrunDetailComponent {
  urun = input<IUrun | null>(null);

  previousState(): void {
    window.history.back();
  }
}
