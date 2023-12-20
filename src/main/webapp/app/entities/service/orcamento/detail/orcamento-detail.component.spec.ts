import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrcamentoDetailComponent } from './orcamento-detail.component';

describe('Orcamento Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrcamentoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OrcamentoDetailComponent,
              resolve: { orcamento: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(OrcamentoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load orcamento on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OrcamentoDetailComponent);

      // THEN
      expect(instance.orcamento).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
