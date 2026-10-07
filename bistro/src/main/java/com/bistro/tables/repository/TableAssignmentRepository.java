package com.bistro.tables.repository;

import com.bistro.tables.model.TableAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TableAssignmentRepository extends JpaRepository<TableAssignment, Long> {

    boolean existsByTableIdAndReservationTime(Long tableId, LocalDateTime reservationTime);
}