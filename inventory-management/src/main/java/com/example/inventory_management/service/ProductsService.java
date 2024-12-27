package com.example.inventory_management.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inventory_management.dto.GetProductsDto;
import com.example.inventory_management.dto.ProductsDto;
import com.example.inventory_management.repository.GetProductsRepository;
import com.example.inventory_management.repository.ProductsRepository;
import com.example.inventory_management.response.RegisterResponse;

@Service
public class ProductsService {

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private GetProductsRepository getProductsRepository;

	// 登録メソッド
	public RegisterResponse register(ProductsDto register) {
		// 商品情報を保存
		ProductsDto savedProduct = productsRepository.save(register);

		// レスポンスを作成して返す
		return new RegisterResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getDescription(),
				savedProduct.getPrice(), savedProduct.getCategoryId(), savedProduct.getMinStockLevel(),
				savedProduct.getCreatedAt());
	}

	// 商品情報をIDで取得するメソッド
	public GetProductsDto getProductById(UUID productId) {
		// IDで商品を検索
		GetProductsDto product = getProductsRepository.findById(productId).orElse(null);
		// 商品が見つからない場合は例外をスロー
		if (product == null) {
			throw new IllegalArgumentException("商品が見つかりません");
		}
		return product;
	}

}
