package com.example.inventory_management.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author 芳末拓也
 *
 *         在庫情報のレスポンスデータを表すクラス
 *
 */
@Getter
@Setter
public class StockResponse {
	private UUID productId;
	private Long currentStock;
	private int minStockLevel;
	private LocalDateTime lastUpdated= LocalDateTime.now();

	public StockResponse(UUID productId, Long currentStock, int minStockLevel, LocalDateTime lastUpdated) {
		this.productId = productId;
		this.currentStock = currentStock;
		this.minStockLevel = minStockLevel;
		this.lastUpdated = lastUpdated;
	}

}
