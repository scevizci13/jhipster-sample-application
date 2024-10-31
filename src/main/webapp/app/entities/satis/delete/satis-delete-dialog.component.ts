import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISatis } from '../satis.model';
import { SatisService } from '../service/satis.service';

@Component({
  standalone: true,
  templateUrl: './satis-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SatisDeleteDialogComponent {
  satis?: ISatis;

  protected satisService = inject(SatisService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.satisService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
