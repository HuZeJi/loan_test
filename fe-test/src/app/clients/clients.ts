import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ClientApi } from './client-api';
import { Client } from '../models';

@Component({
  selector: 'app-clients',
  imports: [
    FormsModule,
    RouterLink,
    MatTableModule,
    MatPaginatorModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
  ],
  templateUrl: './clients.html',
  styleUrl: './clients.scss',
})
export class Clients implements OnInit {
  clients = signal<Client[]>([]);
  total = signal(0);
  showForm = signal(false);
  error = signal('');

  pageIndex = 0;
  pageSize = 10;
  columns = ['name', 'email', 'phoneNumber', 'active', 'actions'];

  editingId: string | null = null;
  form: Partial<Client> = {};

  constructor(private api: ClientApi) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.api.list(this.pageIndex, this.pageSize).subscribe((res) => {
      this.clients.set(res.content);
      this.total.set(res.totalElements);
    });
  }

  onPage(event: PageEvent) {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.load();
  }

  newClient() {
    this.editingId = null;
    this.form = {};
    this.showForm.set(true);
  }

  edit(client: Client) {
    this.editingId = client.id;
    this.form = { ...client };
    this.showForm.set(true);
  }

  save() {
    this.error.set('');
    const req = this.editingId
      ? this.api.update(this.editingId, this.form)
      : this.api.create(this.form);

    req.subscribe({
      next: () => {
        this.showForm.set(false);
        this.load();
      },
      error: (err) => this.error.set(err.error?.message ?? 'No se pudo guardar el cliente'),
    });
  }

  remove(client: Client) {
    if (!confirm(`Desactivar a ${client.name} ${client.lastName}?`)) return;
    this.api.delete(client.id).subscribe(() => this.load());
  }

  cancel() {
    this.showForm.set(false);
  }
}
