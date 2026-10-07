package com.bistro.tables.service;

import com.bistro.tables.model.Table;
import com.bistro.tables.model.TableAssignment;
import com.bistro.tables.repository.TableAssignmentRepository;
import com.bistro.tables.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TableServiceImpl implements TableService {

    private final TableRepository tableRepository;
    private final TableAssignmentRepository assignmentRepository;

    @Override
    public List<Table> findCandidateTables(int partySize) {
        return tableRepository.findByCapacityGreaterThanEqualOrderByCapacityAsc(partySize);
    }

    @Transactional
    @Override
    public Optional<Table> lockTable(Long tableId) {
        return tableRepository.findByIdForUpdate(tableId);
    }

    @Transactional
    @Override
    public Optional<Table> assignTableFor(Long reservationId, int partySize, LocalDateTime reservationTime) {

        for( Table candidate : findCandidateTables(partySize)){
            lockTable(candidate.getId());

            boolean taken = assignmentRepository.existsByTableIdAndReservationTime(candidate.getId(), reservationTime);

            if(!taken){
                assignmentRepository.save(new TableAssignment(
                        candidate.getId(), reservationId, reservationTime
                ));
                return Optional.of(candidate);
            }
        }
        return Optional.empty();
    }
}















