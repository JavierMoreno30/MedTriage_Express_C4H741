export type Prioridad = 'ALTA' | 'MEDIA' | 'BAJA';
export type EstadoCita = 'PENDIENTE' | 'EN_ATENCION' | 'ATENDIDA' | 'CANCELADA';

export interface Doctor {
  id: number;
  nombre: string;
  especialidad: string;
  disponible: boolean;
}

export interface Paciente {
  id: number;
  identificacion: string;
  nombreCompleto: string;
  correo: string;
  telefono: string;
}

export interface CitaMedica {
  id: number;
  codigoCita: string;
  nombrePaciente: string;
  nombreDoctor: string;
  nivelPrioridad: Prioridad;
  motivoConsulta: string;
  estado: EstadoCita;
  montoConsulta: number;
  fechaCita: string;
}

export interface CrearCitaPayload {
  pacienteId: number;
  doctorId: number;
  nivelPrioridad: Prioridad;
  motivoConsulta: string;
  montoConsulta: number;
}
