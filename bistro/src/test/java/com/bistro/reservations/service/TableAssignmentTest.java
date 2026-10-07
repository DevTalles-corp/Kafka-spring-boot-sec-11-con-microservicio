package com.bistro.reservations.service;

import com.bistro.reservations.controller.ReservationRequest;
import com.bistro.reservations.model.ReservationStatus;
import com.bistro.reservations.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("dev")
public class TableAssignmentTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void shouldNotAssignSameTableTwiceForSameSlot(){

        ReservationRequest request = ReservationRequest.builder()
                .reservationTime(LocalDateTime.of(2026, 9, 15, 21, 0))
                .partySize(8)
                .build();

        CustomerIdentity ana = new CustomerIdentity("ana", "Ana", "ana@example.com");

        String first = reservationService.createReservation(request, ana).getReservationCode();
        String second = reservationService.createReservation(request, ana).getReservationCode();

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() ->
                        assertThat(List.of(statusOf(first), statusOf(second)))
                                .containsExactlyInAnyOrder(ReservationStatus.CONFIRMED, ReservationStatus.REJECTED));

    }

    private ReservationStatus statusOf(String code) {
        return reservationRepository.findByReservationCode(code).orElseThrow().getStatus();
    }

}














