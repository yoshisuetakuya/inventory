package com.example.inventory_management.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author 芳末拓也
 *
 *         入出荷情報のレスポンスデータを表すクラス
 *
 */
@Getter
@Setter
public class ResponseInOut {
	private UUID transactionId;
	private UUID productId;
	private Integer quantity;
	private Long currentStock;
	private String transactionType;
	private LocalDateTime timestamp = LocalDateTime.now();

	public ResponseInOut(UUID transactionId, UUID productId, Integer quantity, Long currentStock,
			String transactionType, LocalDateTime timestamp) {
		this.transactionId = transactionId;
		this.productId = productId;
		this.quantity = quantity;
		this.currentStock = currentStock;
		this.transactionType = transactionType;
		this.timestamp = timestamp;
	}

}
