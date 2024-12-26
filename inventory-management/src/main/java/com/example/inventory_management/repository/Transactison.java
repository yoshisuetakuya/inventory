package com.example.inventory_management.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory_management.dto.TransactionDto;

public interface Transactison extends JpaRepository<TransactionDto, UUID> {
	List<TransactionDto> findByProductIdAndTimestampBetween(UUID productId, LocalDateTime from, LocalDateTime to);

}
