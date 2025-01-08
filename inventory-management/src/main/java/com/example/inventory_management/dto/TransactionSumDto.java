package com.example.inventory_management.dto;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author 芳末拓也
 *
 *         入荷と出荷の各合計情報を格納するためのDTOクラス
 *
 */
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
