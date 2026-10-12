package com.medtriage.service;

import com.medtriage.dto.CitaMedicaDTO;
import com.medtriage.dto.CrearCitaDTO;
import com.medtriage.exception.DoctorNoDisponibleException;
import com.medtriage.exception.RecursoNoEncontradoException;
import com.medtriage.model.CitaMedica;
import com.medtriage.model.Doctor;
import com.medtriage.model.Paciente;
import com.medtriage.repository.CitaMedicaRepository;
import com.medtriage.repository.DoctorRepository;
import com.medtriage.repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class CitaMedicaService {

    private static final Set<String> PRIORIDADES = Set.of("ALTA", "MEDIA", "BAJA");
    private static final Set<String> ESTADOS = Set.of("EN_ATENCION", "ATENDIDA", "CANCELADA");

    private final CitaMedicaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final DoctorRepository doctorRepository;

    public CitaMedicaService(CitaMedicaRepository citaRepository,
                             PacienteRepository pacienteRepository,
                             DoctorRepository doctorRepository) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.doctorRepository = doctorRepository;
    }

    public List<CitaMedicaDTO> obtenerTodas() {
        return citaRepository.findAllByOrderByFechaCitaDesc()
                .stream().map(this::toDTO).toList();
    }

    public List<CitaMedicaDTO> obtenerPorPrioridad(String prioridad) {
        String p = prioridad.toUpperCase();
        if (!PRIORIDADES.contains(p)) {
            throw new IllegalArgumentException("Prioridad inválida: " + prioridad);
        }
        return citaRepository.findByNivelPrioridadOrderByFechaCitaDesc(p)
                .stream().map(this::toDTO).toList();
    }

    @Transactional
    public CitaMedicaDTO crearCita(CrearCitaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el paciente con id " + dto.pacienteId()));

        Doctor doctor = doctorRepository.findById(dto.doctorId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el doctor con id " + dto.doctorId()));

        if (!doctor.isDisponible()) {
            throw new DoctorNoDisponibleException(
                    "El doctor " + doctor.getNombre() + " no está disponible");
        }

        CitaMedica cita = new CitaMedica();
        cita.setCodigoCita(generarCodigo());
        cita.setPaciente(paciente);
        cita.setDoctor(doctor);
        cita.setNivelPrioridad(dto.nivelPrioridad());
        cita.setMotivoConsulta(dto.motivoConsulta());
        cita.setMontoConsulta(dto.montoConsulta());
        cita.setEstado("PENDIENTE");
        cita.setFechaCita(LocalDateTime.now());

        return toDTO(citaRepository.save(cita));
    }

    @Transactional
    public CitaMedicaDTO actualizarEstado(Long id, String nuevoEstado) {
        String estado = nuevoEstado.toUpperCase();
        if (!ESTADOS.contains(estado)) {
            throw new IllegalArgumentException(
                    "Estado inválido. Use EN_ATENCION, ATENDIDA o CANCELADA");
        }
        CitaMedica cita = citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la cita con id " + id));
        cita.setEstado(estado);
        return toDTO(citaRepository.save(cita));
    }

    private String generarCodigo() {
        long numero = citaRepository.count() + 1;
        String codigo;
        do {
            codigo = String.format("CIT-2026-%04d", numero++);
        } while (citaRepository.existsByCodigoCita(codigo));
        return codigo;
    }

    private CitaMedicaDTO toDTO(CitaMedica c) {
        return new CitaMedicaDTO(
                c.getId(),
                c.getCodigoCita(),
                c.getPaciente().getNombreCompleto(),
                c.getDoctor().getNombre(),
                c.getNivelPrioridad(),
                c.getMotivoConsulta(),
                c.getEstado(),
                c.getMontoConsulta(),
                c.getFechaCita());
    }
}
