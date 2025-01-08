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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 *
 * @author 芳末拓也
 *
 *         在庫照会APIコントローラクラス
 *
 */
@RestController
public class InventoryInfomationController {

	@Autowired
	private InventoryInfomationService inventoryInfomationService;

	/**
	 * 指定した商品IDの現在の在庫情報を取得するメソッド
	 *
	 * @param productId
	 * @return 現在の在庫情報
	 */
	@Operation(summary = "在庫情報取得", description = "指定した商品IDの現在の在庫情報を取得する")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "在庫情報の取得に成功"),
			@ApiResponse(responseCode = "400", description = "リクエストパラメータが不正"),
			@ApiResponse(responseCode = "404", description = "指定した商品IDが存在しない場合"),
			@ApiResponse(responseCode = "500", description = "サーバーエラー") })
	@GetMapping("/api/v1/inventory/stock")
	public ResponseEntity<StockResponse> getStock(
			@Parameter(description = "商品ID") @RequestParam("productId") UUID productId) {
		final StockResponse stock = inventoryInfomationService.getStockByProductId(productId);
		return ResponseEntity.ok(stock);
	}

	/**
	 * 期間内の取引情報取得するメソッド
	 *
	 * @param productId
	 * @param fromDate
	 * @param toDate
	 * @return 期間内の取引情報
	 */
	@Operation(summary = "期間内の取引情報取得", description = "指定した商品IDと期間に対応する取引情報を取得する")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "取引情報の取得に成功"),
			@ApiResponse(responseCode = "400", description = "リクエストパラメータが不正"),
			@ApiResponse(responseCode = "404", description = "指定した商品IDが存在しません"),
			@ApiResponse(responseCode = "500", description = "サーバーエラー") })
	@GetMapping("/api/v1/inventory/transactions")
	public ResponseEntity<TransactionResponse> getTransactions(
			@Parameter(description = "商品ID") @RequestParam("productId") UUID productId,
			@Parameter(description = "開始日") @RequestParam("fromDate") String fromDate,
			@Parameter(description = "終了日") @RequestParam("toDate") String toDate) {
		// 日付型をパース
		final LocalDateTime from = LocalDateTime.parse(fromDate);
		final LocalDateTime to = LocalDateTime.parse(toDate);
		// 取得
		final List<TransactionDto> transactions = inventoryInfomationService.getInventoryTransactions(productId, from,
				to);
		// TransactionResponseで作成して返す
		return ResponseEntity.ok(new TransactionResponse(transactions, transactions.size()));
	}

}
