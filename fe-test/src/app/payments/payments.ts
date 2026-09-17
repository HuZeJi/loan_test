import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { PaymentApi } from './payment-api';
import { LoanPayment } from '../models';
import { Auth } from '../auth/auth';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

@Component({
  selector: 'app-payments',
  imports: [
    FormsModule,
    RouterLink,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatSlideToggleModule,
  ],
  templateUrl: './payments.html',
  styleUrl: './payments.scss',
})
export class Payments implements OnInit {
  // Signals: state assigned from HTTP subscribe callbacks needs to be a signal
  // for the zoneless app to re-render once the response arrives.
  payments = signal<LoanPayment[]>([]);
  total = signal(0);
  showForm = signal(false);
  error = signal('');

  loanId = '';
  pageIndex = 0;
  pageSize = 10;
  columns = ['paymentDate', 'paymentMethod', 'amount'];

  
  clientId = '';

  newAmount: number | null = null;
  newMethod = '';

  constructor(private route: ActivatedRoute, private api: PaymentApi, private auth: Auth) {}

  ngOnInit() {
    this.loanId = this.route.snapshot.paramMap.get('loanId')!;
    this.clientId = this.route.snapshot.paramMap.get('clientId')!;
    this.load();
  }

  load() {
    this.api.listByLoan(this.loanId, this.pageIndex, this.pageSize).subscribe((res) => {
      this.payments.set(res.content);
      this.total.set(res.totalElements);
    });
  }

  onPage(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.load();
  }

  newPayment() {
    this.newAmount = null;
    this.newMethod = '';
    this.showForm.set(true);
  }

  register() {
    this.error.set('');
    this.api
      .register(this.loanId, {
        amount: this.newAmount!,
        paymentMethod: this.newMethod,
        userId: this.auth.userId() ?? '',
      })
      .subscribe({
        next: () => {
          this.showForm.set(false);
          this.load();
        },
        error: (err) => this.error.set(err.error?.message ?? 'No se pudo registrar el pago'),
      });
  }

  cancel() {
    this.showForm.set(false);
  }
}
