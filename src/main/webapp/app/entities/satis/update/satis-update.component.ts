import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMusteri } from 'app/entities/musteri/musteri.model';
import { MusteriService } from 'app/entities/musteri/service/musteri.service';
import { IUrun } from 'app/entities/urun/urun.model';
import { UrunService } from 'app/entities/urun/service/urun.service';
import { SatisService } from '../service/satis.service';
import { ISatis } from '../satis.model';
import { SatisFormGroup, SatisFormService } from './satis-form.service';

@Component({
  standalone: true,
  selector: 'jhi-satis-update',
  templateUrl: './satis-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SatisUpdateComponent implements OnInit {
  isSaving = false;
  satis: ISatis | null = null;

  musterisSharedCollection: IMusteri[] = [];
  urunsSharedCollection: IUrun[] = [];

  protected satisService = inject(SatisService);
  protected satisFormService = inject(SatisFormService);
  protected musteriService = inject(MusteriService);
  protected urunService = inject(UrunService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SatisFormGroup = this.satisFormService.createSatisFormGroup();

  compareMusteri = (o1: IMusteri | null, o2: IMusteri | null): boolean => this.musteriService.compareMusteri(o1, o2);

  compareUrun = (o1: IUrun | null, o2: IUrun | null): boolean => this.urunService.compareUrun(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ satis }) => {
      this.satis = satis;
      if (satis) {
        this.updateForm(satis);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const satis = this.satisFormService.getSatis(this.editForm);
    if (satis.id !== null) {
      this.subscribeToSaveResponse(this.satisService.update(satis));
    } else {
      this.subscribeToSaveResponse(this.satisService.create(satis));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISatis>>): void {
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

  protected updateForm(satis: ISatis): void {
    this.satis = satis;
    this.satisFormService.resetForm(this.editForm, satis);

    this.musterisSharedCollection = this.musteriService.addMusteriToCollectionIfMissing<IMusteri>(
      this.musterisSharedCollection,
      satis.musteri,
    );
    this.urunsSharedCollection = this.urunService.addUrunToCollectionIfMissing<IUrun>(this.urunsSharedCollection, satis.urun);
  }

  protected loadRelationshipsOptions(): void {
    this.musteriService
      .query()
      .pipe(map((res: HttpResponse<IMusteri[]>) => res.body ?? []))
      .pipe(map((musteris: IMusteri[]) => this.musteriService.addMusteriToCollectionIfMissing<IMusteri>(musteris, this.satis?.musteri)))
      .subscribe((musteris: IMusteri[]) => (this.musterisSharedCollection = musteris));

    this.urunService
      .query()
      .pipe(map((res: HttpResponse<IUrun[]>) => res.body ?? []))
      .pipe(map((uruns: IUrun[]) => this.urunService.addUrunToCollectionIfMissing<IUrun>(uruns, this.satis?.urun)))
      .subscribe((uruns: IUrun[]) => (this.urunsSharedCollection = uruns));
  }
}
