package com.bistro.tables.service;

import com.bistro.tables.model.Table;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TableService {
    List<Table> findCandidateTables(int partySize);

    @Transactional
    Optional<Table> lockTable(Long tableId);

    @Transactional
    Optional<Table> assignTableFor(Long reservationId, int partySize, LocalDateTime reservationTime);
}
