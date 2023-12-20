import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CategoriaDetailComponent } from './categoria-detail.component';

describe('Categoria Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CategoriaDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CategoriaDetailComponent,
              resolve: { categoria: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CategoriaDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load categoria on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CategoriaDetailComponent);

      // THEN
      expect(instance.categoria).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
