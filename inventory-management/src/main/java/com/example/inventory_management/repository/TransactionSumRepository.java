package com.example.inventory_management.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.inventory_management.dto.InventoryDataDto;
import com.example.inventory_management.dto.TransactionSumDto;

/**
 *
 * @author 芳末拓也
 *
 *         入荷と出荷の各値の合計数を取得するためのリポジトリインターフェース
 *
 */
public interface TransactionSumRepository extends JpaRepository<InventoryDataDto, UUID> {
	/**
	 *
	 * @param productId
	 * @return 入荷と出荷の各値の合計
	 */
	@Query(value = """
			SELECT
			    SUM(quantity) as quantity,
			    transaction_type as transactionType
			FROM
			    inventory_data
			WHERE
			    product_id = :productId
			GROUP BY
			    transaction_type
			""", nativeQuery = true)
	List<TransactionSumDto> getQuantityByTransactionType(@Param("productId") UUID productId);

}
