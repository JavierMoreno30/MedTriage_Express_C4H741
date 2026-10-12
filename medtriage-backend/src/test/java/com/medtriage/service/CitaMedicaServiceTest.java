package com.medtriage.service;

import com.medtriage.dto.CitaMedicaDTO;
import com.medtriage.dto.CrearCitaDTO;
import com.medtriage.exception.DoctorNoDisponibleException;
import com.medtriage.model.CitaMedica;
import com.medtriage.model.Doctor;
import com.medtriage.model.Paciente;
import com.medtriage.repository.CitaMedicaRepository;
import com.medtriage.repository.DoctorRepository;
import com.medtriage.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CitaMedicaServiceTest {

    @Mock
    private CitaMedicaRepository citaRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private CitaMedicaService service;

    private Paciente paciente() {
        Paciente p = new Paciente();
        p.setId(1L);
        p.setNombreCompleto("Elena Madrigal Castro");
        return p;
    }

    private Doctor doctor(boolean disponible) {
        Doctor d = new Doctor();
        d.setId(1L);
        d.setNombre("Dr. Carlos Alvarado");
        d.setDisponible(disponible);
        return d;
    }

    @Test
    void crearCita_conDoctorDisponible_guardaCitaPendiente() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente()));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor(true)));
        when(citaRepository.save(any(CitaMedica.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        CrearCitaDTO dto = new CrearCitaDTO(1L, 1L, "ALTA", "Fiebre alta", new BigDecimal("25000"));

        CitaMedicaDTO resultado = service.crearCita(dto);

        assertEquals("PENDIENTE", resultado.estado());
        assertTrue(resultado.codigoCita().startsWith("CIT-2026-"));
        assertEquals("Elena Madrigal Castro", resultado.nombrePaciente());
        verify(citaRepository, times(1)).save(any(CitaMedica.class));
    }

    @Test
    void crearCita_conDoctorNoDisponible_lanzaExcepcion() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente()));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor(false)));

        CrearCitaDTO dto = new CrearCitaDTO(1L, 1L, "ALTA", "Fiebre alta", new BigDecimal("25000"));

        assertThrows(DoctorNoDisponibleException.class, () -> service.crearCita(dto));
        verify(citaRepository, never()).save(any());
    }
}
