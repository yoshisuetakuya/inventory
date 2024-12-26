package com.example.inventory_management.dto;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "products")
public class GetProductsDto {
	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "name")
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private Integer price;

	@Column(name = "category_id")
	private Integer categoryId;

	@Column(name = "current_stock")
	private Integer currentStock;

	@Column(name = "min_stock_level")
	private Integer minStockLevel;

}
