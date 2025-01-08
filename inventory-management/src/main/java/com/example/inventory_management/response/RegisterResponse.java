package com.example.inventory_management.response;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author 芳末拓也
 *
 *         商品登録のレスポンスデータを表すクラス
 *
 */
@Getter
@Setter
public class RegisterResponse {
	private UUID id;
	private String name;
	private String description;
	private Integer price;
	private Integer categoryId;
	private Integer minStockLevel;
	private LocalDateTime createdAt = LocalDateTime.now();

	public RegisterResponse(UUID id, String name, String description, Integer price, Integer categoryId,
			Integer minStockLevel, LocalDateTime createdAt) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.categoryId = categoryId;
		this.minStockLevel = minStockLevel;
		this.createdAt = createdAt;
	}

}
