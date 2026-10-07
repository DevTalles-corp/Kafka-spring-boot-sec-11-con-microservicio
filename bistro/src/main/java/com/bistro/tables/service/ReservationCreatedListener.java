package com.bistro.tables.service;

import com.bistro.tables.events.TableAssigned;
import com.bistro.tables.events.TableRequested;
import com.bistro.tables.events.TableUnavailable;
import com.bistro.tables.idempotency.ProcessedEvent;
import com.bistro.tables.idempotency.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationCreatedListener {

    private final TableService tableService;
    private final ApplicationEventPublisher eventPublisher;
    private final ProcessedEventRepository processedEventRepository;

    @ApplicationModuleListener
    public void onReservationCreated(TableRequested event){

        if(processedEventRepository.existsById(event.reservationId())){
            log.info("Reserva {} ya procesada, se ignora el evento repetido",
                    event.reservationId());
            return;
        }

        tableService.assignTableFor(
                event.reservationId(), event.partySize(), event.reservationTime()
        ).ifPresentOrElse(
                table -> {
                    log.info("Reserva {} → mesa {} ({} lugares) asignada",
                            event.reservationId(), table.getTableNumber(), table.getCapacity());

                    eventPublisher.publishEvent(new TableAssigned(
                            event.reservationId(),
                            table.getId(),
                            table.getTableNumber(),
                            LocalDateTime.now()
                    ));

                },

                () -> {
                    log.info("Reserva {} → sin mesa para {} personas",
                            event.reservationId(), event.partySize());

                    eventPublisher.publishEvent(new TableUnavailable(
                            event.reservationId(),
                            "Sin mesa disponible para " + event.partySize() + " personas",
                            LocalDateTime.now()));
                });

        processedEventRepository.save(new ProcessedEvent(event.reservationId()));
    }
}













