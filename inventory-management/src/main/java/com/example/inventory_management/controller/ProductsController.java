package com.example.inventory_management.controller;

import org.springframework.http.HttpHeaders;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory_management.dto.GetProductsDto;
import com.example.inventory_management.dto.ProductsDto;
import com.example.inventory_management.response.RegisterResponse;
import com.example.inventory_management.response.UpdateProductResponse;
import com.example.inventory_management.service.ProductsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

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
			@Parameter(description = "商品ID", required = true) @PathVariable("productId") UUID productId,
			@RequestHeader(value = HttpHeaders.IF_NONE_MATCH, required = false) String ifNoneMatch) {
		try {
			// 商品情報を取得
			ProductsDto product = productsService.getProductById(productId);

			// 商品が見つからない場合
			if (product == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			// Etagを生成
			String etag = "\"" + productsService.generateEtag(product) + "\"";
			// ProductsDtoをGetProductsDtoに変換
			GetProductsDto getProductDto = productsService.convertToGetProductsDto(product);

			// If-None-Matchヘッダーと比較して、Etagが一致する場合は304を返す
			if (etag.equals(ifNoneMatch)) {
				return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
			}

			// 更新があった場合は、新しくレスポンスにEtagヘッダーを追加して商品を返す
			return ResponseEntity.ok().eTag(etag).body(getProductDto);

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
	public ResponseEntity<RegisterResponse> registerProducts(@Valid @RequestBody ProductsDto dto) {
		// 登録処理
		RegisterResponse savedProduct = productsService.register(dto);
		// 登録された商品情報を返す
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
	}

	/**
	 * 商品情報を更新するメソッド
	 *
	 * @param productId
	 * @param dto
	 * @return 更新結果レスポンス
	 */
	@Operation(summary = "商品情報を更新", description = "指定された商品IDの商品情報を更新する")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "商品情報の更新に成功"),
			@ApiResponse(responseCode = "404", description = "指定された商品が見つからない場合"),
			@ApiResponse(responseCode = "400", description = "入力データが不正な場合"),
			@ApiResponse(responseCode = "500", description = "サーバーエラー") })
	@PutMapping("/api/v1/products/{productId}")
	public ResponseEntity<UpdateProductResponse> updateProduct(
			@Parameter(description = "商品ID", required = true) @PathVariable("productId") UUID productId,
			@Valid @RequestBody ProductsDto dto) {
		try {
	        // 商品情報の更新
	        UpdateProductResponse updatedProduct = productsService.updateProduct(productId, dto);
	        return ResponseEntity.ok(updatedProduct);

	    } catch (OptimisticLockingFailureException e) {
	        // 楽観ロック競合が発生した場合
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
	    }
	}

}
