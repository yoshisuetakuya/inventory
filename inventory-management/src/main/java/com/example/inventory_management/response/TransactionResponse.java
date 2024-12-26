package com.example.inventory_management.response;

import java.util.List;

import com.example.inventory_management.dto.TransactionDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {
	private List<TransactionDto> transactions;
	private int totalCount;

	public TransactionResponse(List<TransactionDto> transactions, int totalCount) {
		this.transactions = transactions;
		this.totalCount = totalCount;
	}

}
