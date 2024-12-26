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

@RestController
public class ProductsController {

	@Autowired
	private ProductsService productsService;

	// 商品情報を取得するAPI
	@GetMapping("/api/v1/products/{productId}")
	public ResponseEntity<GetProductsDto> getProductById(@PathVariable("productId") UUID productId) {
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

	// 商品登録API
	@PostMapping("/api/v1/products")
	public ResponseEntity<RegisterResponse> registerProducts(@RequestBody ProductsDto dto) {
		// 登録処理
		RegisterResponse savedProduct = productsService.register(dto);
		// 登録された商品情報を返す
		return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
	}

}
