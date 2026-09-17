export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export interface Client {
  id: string;
  name: string;
  lastName: string;
  birthday: string;
  address?: string;
  email: string;
  phoneNumber: string;
  active: boolean;
}

export type LoanApplicationStatus = 'PENDING' | 'APPROVED' | 'REJECTED';
export type LoanPaymentStatus = 'PENDING' | 'PARTIALLY_PAID' | 'PAID';

export interface Loan {
  id: string;
  amount: number;
  pendingAmount: number;
  applicationStatus: LoanApplicationStatus;
  paymentStatus: LoanPaymentStatus;
  requestDate?: string;
  resolutionNotes?: string;
  resolutionDate?: string;
  loanTermId: string;
  clientId: string;
  active: boolean;
}

export interface LoanTerm {
  id: string;
  description: string;
  days: number;
}

export interface LoanPayment {
  id: string;
  paymentDate: string;
  paymentMethod: string;
  amount: number;
  loanId: string;
  userId: string;
}
