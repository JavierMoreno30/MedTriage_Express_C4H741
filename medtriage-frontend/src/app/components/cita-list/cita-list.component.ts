import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { CitaService } from '../../services/cita.service';
import { CitaMedica, EstadoCita } from '../../models/cita.model';

type FiltroPrioridad = 'TODAS' | 'ALTA' | 'MEDIA' | 'BAJA';

@Component({
  selector: 'app-cita-list',
  standalone: true,
  imports: [CurrencyPipe, DatePipe],
  templateUrl: './cita-list.component.html',
  styleUrl: './cita-list.component.css',
})
export class CitaListComponent implements OnInit {
  private citaService = inject(CitaService);

  readonly filtros: FiltroPrioridad[] = ['TODAS', 'ALTA', 'MEDIA', 'BAJA'];
  citas = signal<CitaMedica[]>([]);
  filtro = signal<FiltroPrioridad>('TODAS');
  error = signal('');

  citasFiltradas = computed(() => {
    const f = this.filtro();
    return f === 'TODAS' ? this.citas() : this.citas().filter((c) => c.nivelPrioridad === f);
  });

  total = computed(() => this.citasFiltradas().length);

  ngOnInit(): void {
    this.cargar();
  }

  cargar(): void {
    this.citaService.getCitas().subscribe({
      next: (data) => this.citas.set(data),
      error: () => this.error.set('No se pudo conectar con el servidor'),
    });
  }

  siguienteEstado(estado: EstadoCita): EstadoCita | null {
    if (estado === 'PENDIENTE') return 'EN_ATENCION';
    if (estado === 'EN_ATENCION') return 'ATENDIDA';
    return null;
  }

  avanzar(cita: CitaMedica): void {
    const nuevo = this.siguienteEstado(cita.estado);
    if (!nuevo) return;

    this.citaService.actualizarEstado(cita.id, nuevo).subscribe({
      next: (actualizada) =>
        this.citas.update((lista) => lista.map((c) => (c.id === actualizada.id ? actualizada : c))),
      error: () => this.error.set('No se pudo actualizar el estado'),
    });
  }
}
