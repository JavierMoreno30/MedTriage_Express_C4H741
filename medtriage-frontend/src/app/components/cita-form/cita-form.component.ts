import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CitaService } from '../../services/cita.service';
import { CrearCitaPayload } from '../../models/cita.model';

@Component({
  selector: 'app-cita-form',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './cita-form.component.html',
  styleUrl: './cita-form.component.css',
})
export class CitaFormComponent {
  private citaService = inject(CitaService);
  private router = inject(Router);

  error = signal('');
  enviando = signal(false);

  cita: CrearCitaPayload = {
    pacienteId: 1,
    doctorId: 1,
    nivelPrioridad: 'MEDIA',
    motivoConsulta: '',
    montoConsulta: 0,
  };

  guardar(): void {
    this.enviando.set(true);
    this.error.set('');

    this.citaService.crearCita(this.cita).subscribe({
      next: () => this.router.navigate(['/citas']),
      error: (err) => {
        this.error.set(err.error?.detail ?? 'No se pudo registrar la cita');
        this.enviando.set(false);
      },
    });
  }
}
