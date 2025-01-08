package com.example.inventory_management.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author 芳末拓也
 *
 *         在庫管理の取引データを表すDTOクラス
 *
 */
@Entity
@Getter
@Setter
@Table(name = "inventory_data")
public class TransactionDto {
	@Id
	@Column(name = "transaction_id")
	private UUID transactionId;

	@Column(name = "product_id")
	private UUID productId;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "note")
	private String note;

	@Column(name = "timestamp")
	private LocalDateTime timestamp = LocalDateTime.now();
}
