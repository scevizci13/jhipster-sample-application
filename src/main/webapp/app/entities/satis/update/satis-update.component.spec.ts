import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IMusteri } from 'app/entities/musteri/musteri.model';
import { MusteriService } from 'app/entities/musteri/service/musteri.service';
import { IUrun } from 'app/entities/urun/urun.model';
import { UrunService } from 'app/entities/urun/service/urun.service';
import { ISatis } from '../satis.model';
import { SatisService } from '../service/satis.service';
import { SatisFormService } from './satis-form.service';

import { SatisUpdateComponent } from './satis-update.component';

describe('Satis Management Update Component', () => {
  let comp: SatisUpdateComponent;
  let fixture: ComponentFixture<SatisUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let satisFormService: SatisFormService;
  let satisService: SatisService;
  let musteriService: MusteriService;
  let urunService: UrunService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SatisUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SatisUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SatisUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    satisFormService = TestBed.inject(SatisFormService);
    satisService = TestBed.inject(SatisService);
    musteriService = TestBed.inject(MusteriService);
    urunService = TestBed.inject(UrunService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Musteri query and add missing value', () => {
      const satis: ISatis = { id: 456 };
      const musteri: IMusteri = { id: 26924 };
      satis.musteri = musteri;

      const musteriCollection: IMusteri[] = [{ id: 15210 }];
      jest.spyOn(musteriService, 'query').mockReturnValue(of(new HttpResponse({ body: musteriCollection })));
      const additionalMusteris = [musteri];
      const expectedCollection: IMusteri[] = [...additionalMusteris, ...musteriCollection];
      jest.spyOn(musteriService, 'addMusteriToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ satis });
      comp.ngOnInit();

      expect(musteriService.query).toHaveBeenCalled();
      expect(musteriService.addMusteriToCollectionIfMissing).toHaveBeenCalledWith(
        musteriCollection,
        ...additionalMusteris.map(expect.objectContaining),
      );
      expect(comp.musterisSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Urun query and add missing value', () => {
      const satis: ISatis = { id: 456 };
      const urun: IUrun = { id: 9291 };
      satis.urun = urun;

      const urunCollection: IUrun[] = [{ id: 21991 }];
      jest.spyOn(urunService, 'query').mockReturnValue(of(new HttpResponse({ body: urunCollection })));
      const additionalUruns = [urun];
      const expectedCollection: IUrun[] = [...additionalUruns, ...urunCollection];
      jest.spyOn(urunService, 'addUrunToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ satis });
      comp.ngOnInit();

      expect(urunService.query).toHaveBeenCalled();
      expect(urunService.addUrunToCollectionIfMissing).toHaveBeenCalledWith(
        urunCollection,
        ...additionalUruns.map(expect.objectContaining),
      );
      expect(comp.urunsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const satis: ISatis = { id: 456 };
      const musteri: IMusteri = { id: 4377 };
      satis.musteri = musteri;
      const urun: IUrun = { id: 25867 };
      satis.urun = urun;

      activatedRoute.data = of({ satis });
      comp.ngOnInit();

      expect(comp.musterisSharedCollection).toContain(musteri);
      expect(comp.urunsSharedCollection).toContain(urun);
      expect(comp.satis).toEqual(satis);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISatis>>();
      const satis = { id: 123 };
      jest.spyOn(satisFormService, 'getSatis').mockReturnValue(satis);
      jest.spyOn(satisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ satis });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: satis }));
      saveSubject.complete();

      // THEN
      expect(satisFormService.getSatis).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(satisService.update).toHaveBeenCalledWith(expect.objectContaining(satis));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISatis>>();
      const satis = { id: 123 };
      jest.spyOn(satisFormService, 'getSatis').mockReturnValue({ id: null });
      jest.spyOn(satisService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ satis: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: satis }));
      saveSubject.complete();

      // THEN
      expect(satisFormService.getSatis).toHaveBeenCalled();
      expect(satisService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISatis>>();
      const satis = { id: 123 };
      jest.spyOn(satisService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ satis });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(satisService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMusteri', () => {
      it('Should forward to musteriService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(musteriService, 'compareMusteri');
        comp.compareMusteri(entity, entity2);
        expect(musteriService.compareMusteri).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUrun', () => {
      it('Should forward to urunService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(urunService, 'compareUrun');
        comp.compareUrun(entity, entity2);
        expect(urunService.compareUrun).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
