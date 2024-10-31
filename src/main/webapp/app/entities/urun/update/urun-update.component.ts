import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUrun } from '../urun.model';
import { UrunService } from '../service/urun.service';
import { UrunFormGroup, UrunFormService } from './urun-form.service';

@Component({
  standalone: true,
  selector: 'jhi-urun-update',
  templateUrl: './urun-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UrunUpdateComponent implements OnInit {
  isSaving = false;
  urun: IUrun | null = null;

  protected urunService = inject(UrunService);
  protected urunFormService = inject(UrunFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UrunFormGroup = this.urunFormService.createUrunFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ urun }) => {
      this.urun = urun;
      if (urun) {
        this.updateForm(urun);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const urun = this.urunFormService.getUrun(this.editForm);
    if (urun.id !== null) {
      this.subscribeToSaveResponse(this.urunService.update(urun));
    } else {
      this.subscribeToSaveResponse(this.urunService.create(urun));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUrun>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(urun: IUrun): void {
    this.urun = urun;
    this.urunFormService.resetForm(this.editForm, urun);
  }
}
