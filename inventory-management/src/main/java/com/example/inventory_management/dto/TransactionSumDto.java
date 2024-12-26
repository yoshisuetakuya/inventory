package com.example.inventory_management.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionSumDto {
	private Long quantity;
	private String transactionType;

	public TransactionSumDto(Long quantity, String transactionType) {
		this.quantity = quantity;
		this.transactionType = transactionType;
	}

}
