package com.example.inventory_management.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory_management.dto.TransactionDto;

/**
 *
 * @author 芳末拓也
 *
 *         取引データを操作するリポジトリインターフェース
 *
 */
public interface Transactison extends JpaRepository<TransactionDto, UUID> {
	/**
	 *
	 * @param productId
	 * @param from
	 * @param to
	 * @return 取引データ
	 */
	List<TransactionDto> findByProductIdAndTimestampBetween(UUID productId, LocalDateTime from, LocalDateTime to);

}
