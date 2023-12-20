import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TransacaoDetailComponent } from './transacao-detail.component';

describe('Transacao Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TransacaoDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TransacaoDetailComponent,
              resolve: { transacao: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TransacaoDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load transacao on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TransacaoDetailComponent);

      // THEN
      expect(instance.transacao).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
