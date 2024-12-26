package com.example.inventory_management.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory_management.dto.TransactionDto;
import com.example.inventory_management.response.TransactionResponse;
import com.example.inventory_management.response.StockResponse;
import com.example.inventory_management.service.*;

@RestController
public class InventoryInfomationController {
	@Autowired
	private InventoryInfomationService inventoryInfomationService;

	// 在庫数取得API
	@GetMapping("/api/v1/inventory/stock")
	public ResponseEntity<StockResponse> getStock(@RequestParam("productId") UUID productId) {
		StockResponse stock = inventoryInfomationService.getStockByProductId(productId);
		return ResponseEntity.ok(stock);
	}

	// 期間内の入出荷情報取得API
	@GetMapping("/api/v1/inventory/transactions")
	public ResponseEntity<TransactionResponse> getTransactions(@RequestParam("productId") UUID productId,
			@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate) {
		// 日付型をパース
		LocalDateTime from = LocalDateTime.parse(fromDate);
		LocalDateTime to = LocalDateTime.parse(toDate);
		// 取得
		List<TransactionDto> transactions = inventoryInfomationService.getInventoryTransactions(productId, from, to);
		// TransactionResponseで作成して返す
		return ResponseEntity.ok(new TransactionResponse(transactions, transactions.size()));
	}

}
