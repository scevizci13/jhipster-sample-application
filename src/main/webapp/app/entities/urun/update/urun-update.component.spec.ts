import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { UrunService } from '../service/urun.service';
import { IUrun } from '../urun.model';
import { UrunFormService } from './urun-form.service';

import { UrunUpdateComponent } from './urun-update.component';

describe('Urun Management Update Component', () => {
  let comp: UrunUpdateComponent;
  let fixture: ComponentFixture<UrunUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let urunFormService: UrunFormService;
  let urunService: UrunService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UrunUpdateComponent],
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
      .overrideTemplate(UrunUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UrunUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    urunFormService = TestBed.inject(UrunFormService);
    urunService = TestBed.inject(UrunService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const urun: IUrun = { id: 456 };

      activatedRoute.data = of({ urun });
      comp.ngOnInit();

      expect(comp.urun).toEqual(urun);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUrun>>();
      const urun = { id: 123 };
      jest.spyOn(urunFormService, 'getUrun').mockReturnValue(urun);
      jest.spyOn(urunService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ urun });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: urun }));
      saveSubject.complete();

      // THEN
      expect(urunFormService.getUrun).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(urunService.update).toHaveBeenCalledWith(expect.objectContaining(urun));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUrun>>();
      const urun = { id: 123 };
      jest.spyOn(urunFormService, 'getUrun').mockReturnValue({ id: null });
      jest.spyOn(urunService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ urun: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: urun }));
      saveSubject.complete();

      // THEN
      expect(urunFormService.getUrun).toHaveBeenCalled();
      expect(urunService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUrun>>();
      const urun = { id: 123 };
      jest.spyOn(urunService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ urun });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(urunService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
