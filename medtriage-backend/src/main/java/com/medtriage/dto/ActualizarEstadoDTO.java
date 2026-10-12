package com.medtriage.dto;
//actualiza el estado de cita
import jakarta.validation.constraints.NotBlank;
public record ActualizarEstadoDTO(@NotBlank String estado) {
}
