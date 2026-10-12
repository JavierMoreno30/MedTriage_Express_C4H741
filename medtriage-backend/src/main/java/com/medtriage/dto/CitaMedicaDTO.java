package com.medtriage.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
//id,codCita,nombrePaciente,nombreDoctor,nivelprioridad,motivoconsulta,estado,montoConsulta
public record CitaMedicaDTO(
        Long id,
        String codigoCita,
        String nombrePaciente,
        String nombreDoctor,
        String nivelPrioridad,
        String motivoConsulta,
        String estado,
        BigDecimal montoConsulta,
        LocalDateTime fechaCita) {
}
