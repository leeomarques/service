import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IOrcamento } from '../orcamento.model';
import { OrcamentoService } from '../service/orcamento.service';

@Component({
  standalone: true,
  templateUrl: './orcamento-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrcamentoDeleteDialogComponent {
  orcamento?: IOrcamento;

  constructor(
    protected orcamentoService: OrcamentoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orcamentoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
