import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { MusteriService } from '../service/musteri.service';
import { IMusteri } from '../musteri.model';
import { MusteriFormService } from './musteri-form.service';

import { MusteriUpdateComponent } from './musteri-update.component';

describe('Musteri Management Update Component', () => {
  let comp: MusteriUpdateComponent;
  let fixture: ComponentFixture<MusteriUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let musteriFormService: MusteriFormService;
  let musteriService: MusteriService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MusteriUpdateComponent],
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
      .overrideTemplate(MusteriUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MusteriUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    musteriFormService = TestBed.inject(MusteriFormService);
    musteriService = TestBed.inject(MusteriService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const musteri: IMusteri = { id: 456 };

      activatedRoute.data = of({ musteri });
      comp.ngOnInit();

      expect(comp.musteri).toEqual(musteri);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusteri>>();
      const musteri = { id: 123 };
      jest.spyOn(musteriFormService, 'getMusteri').mockReturnValue(musteri);
      jest.spyOn(musteriService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musteri });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: musteri }));
      saveSubject.complete();

      // THEN
      expect(musteriFormService.getMusteri).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(musteriService.update).toHaveBeenCalledWith(expect.objectContaining(musteri));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusteri>>();
      const musteri = { id: 123 };
      jest.spyOn(musteriFormService, 'getMusteri').mockReturnValue({ id: null });
      jest.spyOn(musteriService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musteri: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: musteri }));
      saveSubject.complete();

      // THEN
      expect(musteriFormService.getMusteri).toHaveBeenCalled();
      expect(musteriService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMusteri>>();
      const musteri = { id: 123 };
      jest.spyOn(musteriService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ musteri });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(musteriService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
