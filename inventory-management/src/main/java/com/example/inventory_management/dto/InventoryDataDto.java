package com.example.inventory_management.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "inventory_data")
public class InventoryDataDto {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "transaction_id")
	private UUID transactionId;

	@Column(name = "product_id")
	private UUID productId;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "timestamp")
	private LocalDateTime timestamp = LocalDateTime.now();

	@Column(name = "location_id")
	private String locationId;

	@Column(name = "note")
	private String note;

}