package com.example.inventory_management.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inventory_management.dto.ProductsDto;
import com.example.inventory_management.dto.TransactionDto;
import com.example.inventory_management.dto.TransactionSumDto;
import com.example.inventory_management.repository.ProductsRepository;
import com.example.inventory_management.repository.TransactionSumRepository;
import com.example.inventory_management.repository.Transactison;
import com.example.inventory_management.response.StockResponse;

@Service
public class InventoryInfomationService {

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private TransactionSumRepository transactionSumRepository;

	@Autowired
	private Transactison transactison;

	// 商品IDに基づいて在庫を計算し、現在の在庫数の情報を返す
	public StockResponse getStockByProductId(UUID productId) {
		// 現在の在庫数を取得
		Long currentStock = calculateCurrentStock(productId);

		ProductsDto productInfo = productsRepository.findById(productId).orElse(null);

		// StockResponseを作成して返す
		return new StockResponse(productId, currentStock, productInfo.getMinStockLevel(), productInfo.getLastUpdated());
	}

	// 期間内の入出荷情報取得
	public List<TransactionDto> getInventoryTransactions(UUID productId, LocalDateTime from, LocalDateTime to) {
		return transactison.findByProductIdAndTimestampBetween(productId, from, to);
	}

	// 現在の在庫数の計算メソッド
	public Long calculateCurrentStock(UUID productId) {
		// productIdで検索し合計値を取得
		List<TransactionSumDto> transactionSummaries = transactionSumRepository.getQuantityByTransactionType(productId);

		Long inQuantity = 0L;
		Long outQuantity = 0L;

		// IN とOUT を取得
		for (TransactionSumDto summary : transactionSummaries) {
			if ("IN".equalsIgnoreCase(summary.getTransactionType())) {
				inQuantity = summary.getQuantity();
			} else if ("OUT".equalsIgnoreCase(summary.getTransactionType())) {
				outQuantity = summary.getQuantity();
			}
		}

		// 現在の在庫数 IN - OUT
		return inQuantity - outQuantity;
	}

}
