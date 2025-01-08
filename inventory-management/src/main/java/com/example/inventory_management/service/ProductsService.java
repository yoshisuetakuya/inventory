package com.example.inventory_management.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inventory_management.dto.GetProductsDto;
import com.example.inventory_management.dto.ProductsDto;
import com.example.inventory_management.repository.GetProductsRepository;
import com.example.inventory_management.repository.ProductsRepository;
import com.example.inventory_management.response.RegisterResponse;

/**
 *
 * @author 芳末拓也
 *
 *         商品の情報の登録と取得を行うサービスクラス
 *
 */
@Service
public class ProductsService {

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private GetProductsRepository getProductsRepository;

	/**
	 * 商品情報登録メソッド
	 *
	 * @param register
	 * @return 登録された商品の情報を含むレスポンス
	 */
	public RegisterResponse register(ProductsDto register) {
		// 商品情報を保存
		final ProductsDto savedProduct = productsRepository.save(register);

		// レスポンスを作成して返す
		return new RegisterResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getDescription(),
				savedProduct.getPrice(), savedProduct.getCategoryId(), savedProduct.getMinStockLevel(),
				savedProduct.getCreatedAt());
	}

	/**
	 * 商品情報取得メソッド
	 *
	 * @param productId
	 * @return 指定された商品IDの商品情報
	 */
	public GetProductsDto getProductById(UUID productId) {
		// IDで商品を検索
		final GetProductsDto product = getProductsRepository.findById(productId).orElse(null);
		// 商品が見つからない場合は例外をスロー
		if (product == null) {
			throw new IllegalArgumentException("商品が見つかりません");
		}
		return product;
	}

}
