import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CitaMedica, CrearCitaPayload, EstadoCita } from '../models/cita.model';

@Injectable({ providedIn: 'root' })
export class CitaService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.API_URL}citas`;

  getCitas(): Observable<CitaMedica[]> {
    return this.http.get<CitaMedica[]>(this.baseUrl);
  }

  getCitasPorPrioridad(prioridad: string): Observable<CitaMedica[]> {
    return this.http.get<CitaMedica[]>(`${this.baseUrl}/prioridad/${prioridad}`);
  }

  crearCita(payload: CrearCitaPayload): Observable<CitaMedica> {
    return this.http.post<CitaMedica>(this.baseUrl, payload);
  }

  actualizarEstado(id: number, estado: EstadoCita): Observable<CitaMedica> {
    return this.http.patch<CitaMedica>(`${this.baseUrl}/${id}/estado`, { estado });
  }
}
