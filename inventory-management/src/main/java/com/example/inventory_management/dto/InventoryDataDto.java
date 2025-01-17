package com.example.inventory_management.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author 芳末拓也
 *
 *         在庫データを表すDTOクラス
 *
 *         このクラスはデータベースのinventory_dataテーブルに対応している
 */
@Entity
@Getter
@Setter
@Table(name = "inventory_data")
public class InventoryDataDto {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transaction_id")
	private UUID transactionId;

	@NotNull(message = "商品IDは必須項目です")
	@Column(name = "product_id")
	private UUID productId;

	@Min(value = 0, message = "数量は0以上で入力してください")
	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "timestamp")
	private LocalDateTime timestamp = LocalDateTime.now();

	@Size(max = 50, message = "ロケーションIDは最大50文字までです")
	@Column(name = "location_id")
	private String locationId;

	@Size(max = 200, message = "メモは最大200文字までです")
	@Column(name = "note")
	private String note;

}