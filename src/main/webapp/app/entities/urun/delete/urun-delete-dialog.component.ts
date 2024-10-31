import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUrun } from '../urun.model';
import { UrunService } from '../service/urun.service';

@Component({
  standalone: true,
  templateUrl: './urun-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UrunDeleteDialogComponent {
  urun?: IUrun;

  protected urunService = inject(UrunService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.urunService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
