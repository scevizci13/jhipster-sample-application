import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMusteri } from '../musteri.model';
import { MusteriService } from '../service/musteri.service';
import { MusteriFormGroup, MusteriFormService } from './musteri-form.service';

@Component({
  standalone: true,
  selector: 'jhi-musteri-update',
  templateUrl: './musteri-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MusteriUpdateComponent implements OnInit {
  isSaving = false;
  musteri: IMusteri | null = null;

  protected musteriService = inject(MusteriService);
  protected musteriFormService = inject(MusteriFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MusteriFormGroup = this.musteriFormService.createMusteriFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ musteri }) => {
      this.musteri = musteri;
      if (musteri) {
        this.updateForm(musteri);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const musteri = this.musteriFormService.getMusteri(this.editForm);
    if (musteri.id !== null) {
      this.subscribeToSaveResponse(this.musteriService.update(musteri));
    } else {
      this.subscribeToSaveResponse(this.musteriService.create(musteri));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMusteri>>): void {
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

  protected updateForm(musteri: IMusteri): void {
    this.musteri = musteri;
    this.musteriFormService.resetForm(this.editForm, musteri);
  }
}
