import { Service, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { API_BASE } from '../api-base';
import { LoanPayment, Page } from '../models';

@Service()
export class PaymentApi {
  private http = inject(HttpClient);

  listByLoan(loanId: string, page: number, size: number) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<LoanPayment>>(`${API_BASE}/loan/${loanId}/loan-payment`, { params });
  }

  register(loanId: string, payment: Partial<LoanPayment>) {
    return this.http.post<LoanPayment>(`${API_BASE}/loan/${loanId}/loan-payment`, payment);
  }
}
