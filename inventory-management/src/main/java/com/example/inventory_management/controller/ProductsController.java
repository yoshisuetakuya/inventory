package com.example.inventory_management.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory_management.dto.GetProductsDto;
import com.example.inventory_management.dto.ProductsDto;
import com.example.inventory_management.response.RegisterResponse;
import com.example.inventory_management.service.ProductsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 *
 * @author 芳末拓也
 *
 *         商品管理APIコントローラクラス
 *
 */
@RestController
public class ProductsController {

	@Autowired
	private ProductsService productsService;

	/**
	 * 商品情報を取得するメソッド
	 *
	 * @param productId
	 * @return 指定された商品IDの商品情報
	 */
	@Operation(summary = "商品情報を取得", description = "指定された商品IDに基づいて商品情報を取得する")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "商品情報の取得に成功"),
			@ApiResponse(responseCode = "404", description = "指定された商品が見つからない場合"),
			@ApiResponse(responseCode = "500", description = "サーバーエラー") })

	@GetMapping("/api/v1/products/{productId}")
	public ResponseEntity<GetProductsDto> getProductById(
			@Parameter(description = "商品ID", required = true) @PathVariable("productId") UUID productId) {
		try {
			// 商品情報を取得
			GetProductsDto product = productsService.getProductById(productId);
			// 商品が見つかった場合
			return ResponseEntity.ok(product);
		} catch (IllegalArgumentException e) {
			// 商品が見つからなかった場合
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	/**
	 * 新しい商品を登録するメソッド
	 *
	 * @param dto
	 * @return 登録結果レスポンス
	 */
	@Operation(summary = "商品を登録", description = "新しい商品を登録する")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "商品情報の登録に成功"),
			@ApiResponse(responseCode = "400", description = "入力データが不正な場合"),
			@ApiResponse(responseCode = "500", description = "サーバーエラー") })
	@PostMapping("/api/v1/products")
	public ResponseEntity<RegisterResponse> registerProducts(@RequestBody ProductsDto dto) {
		// 登録処理
		RegisterResponse savedProduct = productsService.register(dto);
		// 登録された商品情報を返す
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
	}

}
