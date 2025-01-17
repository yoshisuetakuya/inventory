package com.example.inventory_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory_management.dto.InventoryDataDto;
import com.example.inventory_management.response.ResponseInOut;
import com.example.inventory_management.service.InventoryDataService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 *
 * @author 芳末 拓也
 *
 *         在庫管理APIコントローラークラス
 *
 */
@RestController
public class InventoryDataController {

	@Autowired
	private InventoryDataService inventoryDataService;

	/**
	 * 商品の入荷を登録するメソッド
	 *
	 * @param dto
	 * @return 入荷処理の結果レスポンス
	 */
	@Operation(summary = "入荷処理", description = "新しい商品の入荷を登録する")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "入荷処理が正常に完了"),
			@ApiResponse(responseCode = "400", description = "リクエストデータが不正"),
			@ApiResponse(responseCode = "500", description = "サーバーエラー") })
	@PostMapping("/api/v1/inventory/in")
	public ResponseEntity<ResponseInOut> arrive(@Valid @RequestBody InventoryDataDto dto) {
		final ResponseInOut arrival = inventoryDataService.arrival(dto);
		return ResponseEntity.status(HttpStatus.OK).body(arrival);

	}

	/**
	 * 商品の出荷を記録するメソッド
	 *
	 * @param dto
	 * @return 出荷処理の結果レスポンス
	 */
	@Operation(summary = "出荷処理", description = "商品の出荷を記録する")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "出荷処理が正常に完了"),
			@ApiResponse(responseCode = "400", description = "リクエストデータが不正"),
			@ApiResponse(responseCode = "500", description = "サーバーエラー") })
	@PostMapping("/api/v1/inventory/out")
	public ResponseEntity<ResponseInOut> ship(@Valid @RequestBody InventoryDataDto dto) {
		final ResponseInOut shipping = inventoryDataService.shipment(dto);
		return ResponseEntity.status(HttpStatus.OK).body(shipping);
	}
}
