import { Service, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { API_BASE } from '../api-base';
import { Loan, LoanTerm, Page } from '../models';

@Service()
export class LoanApi {
  private http = inject(HttpClient);
  private readonly url = `${API_BASE}/loan`;

  listByClient(clientId: string, page: number, size: number) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Loan>>(`${API_BASE}/client/${clientId}/loan`, { params });
  }

  listApprovedByClient(clientId: string, page: number, size: number) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Loan>>(`${API_BASE}/client/${clientId}/loan/approved`, { params });
  }

  create(loan: Partial<Loan>) {
    return this.http.post<Loan>(this.url, loan);
  }

  approve(loanId: string, userId: string, resolutionNotes: string) {
    return this.http.patch<Loan>(`${this.url}/${loanId}/approve`, { userId, resolutionNotes });
  }

  reject(loanId: string, userId: string, resolutionNotes: string) {
    return this.http.patch<Loan>(`${this.url}/${loanId}/reject`, { userId, resolutionNotes });
  }

  listTerms() {
    return this.http.get<LoanTerm[]>(`${API_BASE}/loan-term`);
  }
}
