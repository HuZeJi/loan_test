import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { LoanApi } from './loan-api';
import { Loan, LoanTerm } from '../models';
import { Auth } from '../auth/auth';

@Component({
  selector: 'app-loans',
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
  templateUrl: './loans.html',
  styleUrl: './loans.scss',
})
export class Loans implements OnInit {
  loans = signal<Loan[]>([]);
  terms = signal<LoanTerm[]>([]);
  total = signal(0);
  showForm = signal(false);
  resolvingLoan = signal<Loan | null>(null);
  error = signal('');

  clientId = '';
  pageIndex = 0;
  pageSize = 10;
  onlyApproved = false;
  columns = ['amount', 'pendingAmount', 'applicationStatus', 'paymentStatus', 'actions'];

  newAmount: number | null = null;
  newTermId = '';
  resolutionNotes = '';

  constructor(private route: ActivatedRoute, private api: LoanApi, private auth: Auth) {}

  ngOnInit() {
    this.clientId = this.route.snapshot.paramMap.get('clientId')!;
    this.api.listTerms().subscribe((terms) => this.terms.set(terms));
    this.load();
  }

  load() {
    const req = this.onlyApproved
      ? this.api.listApprovedByClient(this.clientId, this.pageIndex, this.pageSize)
      : this.api.listByClient(this.clientId, this.pageIndex, this.pageSize);

    req.subscribe((res) => {
      this.loans.set(res.content);
      this.total.set(res.totalElements);
    });
  }

  toggleApproved() {
    this.pageIndex = 0;
    this.load();
  }

  onPage(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.load();
  }

  newLoan() {
    this.newAmount = null;
    this.newTermId = '';
    this.showForm.set(true);
  }

  request() {
    this.error.set('');
    this.api
      .create({ amount: this.newAmount!, clientId: this.clientId, loanTermId: this.newTermId })
      .subscribe({
        next: () => {
          this.showForm.set(false);
          this.load();
        },
        error: (err) => this.error.set(err.error?.message ?? 'No se pudo solicitar el prestamo'),
      });
  }

  cancel() {
    this.showForm.set(false);
  }

  startResolve(loan: Loan) {
    this.resolvingLoan.set(loan);
    this.resolutionNotes = '';
  }

  cancelResolve() {
    this.resolvingLoan.set(null);
  }

  approve() {
    const userId = this.auth.userId() ?? '';
    this.api.approve(this.resolvingLoan()!.id, userId, this.resolutionNotes).subscribe(() => {
      this.resolvingLoan.set(null);
      this.load();
    });
  }

  reject() {
    const userId = this.auth.userId() ?? '';
    this.api.reject(this.resolvingLoan()!.id, userId, this.resolutionNotes).subscribe(() => {
      this.resolvingLoan.set(null);
      this.load();
    });
  }
}
