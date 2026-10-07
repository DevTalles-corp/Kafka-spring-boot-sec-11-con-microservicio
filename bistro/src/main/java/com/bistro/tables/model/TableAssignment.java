package com.bistro.tables.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@jakarta.persistence.Table(
        name = "table_assignments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"table_id", "reservation_time"}))
@Getter
@NoArgsConstructor
public class TableAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_id", nullable = false)
    private Long tableId;

    @Column(name = "reservation_id", nullable = false, unique = true)
    private Long reservationId;

    @Column(name = "reservation_time", nullable = false)
    private LocalDateTime reservationTime;

    public TableAssignment(Long tableId, Long reservationId, LocalDateTime reservationTime) {
        this.tableId = tableId;
        this.reservationId = reservationId;
        this.reservationTime = reservationTime;
    }
}