package com.medtriage.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
//establece estado en pendiente y guarda en la base de datos
public record CrearCitaDTO(
        @NotNull(message = "El paciente es obligatorio") Long pacienteId,
        @NotNull(message = "El doctor es obligatorio") Long doctorId,
        @NotBlank @Pattern(regexp = "ALTA|MEDIA|BAJA", message = "La prioridad debe ser ALTA, MEDIA o BAJA")
        String nivelPrioridad,
        @NotBlank @Size(max = 255) String motivoConsulta,
        @NotNull @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
        BigDecimal montoConsulta) {
}
