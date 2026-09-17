import { Service, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { API_BASE } from '../api-base';
import { Client, Page } from '../models';

@Service()
export class ClientApi {
  private http = inject(HttpClient);
  private readonly url = `${API_BASE}/client`;

  list(page: number, size: number) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<Page<Client>>(this.url, { params });
  }

  create(client: Partial<Client>) {
    return this.http.post<Client>(this.url, client);
  }

  update(id: string, client: Partial<Client>) {
    return this.http.put<Client>(`${this.url}/${id}`, client);
  }

  delete(id: string) {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
