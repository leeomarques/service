import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICategoria } from '../categoria.model';
import { CategoriaService } from '../service/categoria.service';

@Component({
  standalone: true,
  templateUrl: './categoria-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CategoriaDeleteDialogComponent {
  categoria?: ICategoria;

  constructor(
    protected categoriaService: CategoriaService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categoriaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
