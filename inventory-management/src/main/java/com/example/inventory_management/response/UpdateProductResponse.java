package com.example.inventory_management.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
*
* @author 芳末拓也
*
*         商品情報更新のレスポンスデータを表すクラス
*
*/
@Getter
@Setter
public class UpdateProductResponse {
	private UUID id;
    private String name;
    private String description;
    private int price;
    private int categoryId;
    private int minStockLevel;
    private LocalDateTime lastUpdated;

    public UpdateProductResponse(UUID id, String name, String description, Integer price, Integer categoryId,
			Integer minStockLevel, LocalDateTime lastUpdated) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.categoryId = categoryId;
		this.minStockLevel = minStockLevel;
		this.lastUpdated = lastUpdated;
	}

}
