package com.example.inventory_management.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author 芳末拓也
 *
 *         商品情報を表すDTOクラス
 *
 *         このクラスはデータベースのproductsテーブルに対応している
 *
 */
@Entity
@Getter
@Setter
@Table(name = "products")
public class ProductsDto {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private UUID id;

	@NotBlank(message = "名前は必須項目です")
	@Size(max = 100, message = "名前は最大100文字までです")
	@Column(name = "name")
	private String name;

	@Size(max = 500, message = "説明は最大500文字までです")
	@Column(name = "description")
	private String description;

	@NotNull(message = "価格は必須項目です")
	@Min(value = 0, message = "価格は0以上で入力してください")
	@Column(name = "price")
	private Integer price;

	@NotNull(message = "カテゴリーIDは必須項目です")
	@Column(name = "category_id")
	private Integer categoryId;

	@Min(value = 0, message = "最低在庫数は0以上で入力してください")
	@Column(name = "min_stock_level")
	private Integer minStockLevel;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Min(value = 0, message = "現在の在庫数は0以上で入力してください")
	@Column(name = "current_stock")
	private Long currentStock = 0L;

	@UpdateTimestamp
	@Column(name = "last_updated")
	private LocalDateTime lastUpdated;

}
