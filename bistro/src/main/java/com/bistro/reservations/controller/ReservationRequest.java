package com.bistro.reservations.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    private static final Set<LocalTime> ALLOWED_SLOTS = Set.of(
            LocalTime.of(12, 0), LocalTime.of(14, 0),
            LocalTime.of(19, 0), LocalTime.of(21, 0));

    @NotNull(message = "La fecha y hora de la reserva son obligatorias")
    private LocalDateTime reservationTime;

    @NotNull(message = "La cantidad de comensales es obligatoria")
    @Positive(message = "La cantidad de comensales debe ser mayor a cero")
    @Max(value = 12, message = "La cantidad de comensales no puede superar las 12 personas")
    private Integer partySize;

    @JsonIgnore
    @AssertTrue(message = "La reserva debe ser en un turno: 12:00, 14:00, 19:00 o 21:00")
    public boolean isAllowedSlot(){
        return reservationTime == null || ALLOWED_SLOTS.contains(reservationTime.toLocalTime());
    }
}











