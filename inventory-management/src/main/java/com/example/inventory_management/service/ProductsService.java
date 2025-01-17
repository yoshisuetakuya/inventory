package com.example.inventory_management.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inventory_management.dto.GetProductsDto;
import com.example.inventory_management.dto.ProductsDto;
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
	public ProductsDto getProductById(UUID productId) {
		// IDで商品を検索
		final ProductsDto product = productsRepository.findById(productId).orElse(null);
		// 商品が見つからない場合は例外をスロー
		if (product == null) {
			throw new IllegalArgumentException("商品が見つかりません");
		}
		return product;
	}

	/**
	 * データベースの更新日時を基にEtagを生成するメソッド
	 *
	 * @param product
	 * @return
	 */
	public String generateEtag(ProductsDto product) {
		// 更新日時を使ってEtagを生成
		LocalDateTime updated = product.getLastUpdated();
		return product.getId().toString() + "-" + updated;
	}

	/**
	 * ProductsDto型からGetProductsDtoに変換するメソッド
	 *
	 * @param product
	 * @return
	 */
	public GetProductsDto convertToGetProductsDto(ProductsDto product) {
		GetProductsDto getProductDto = new GetProductsDto();
		getProductDto.setId(product.getId());
		getProductDto.setName(product.getName());
		getProductDto.setDescription(product.getDescription());
		getProductDto.setPrice(product.getPrice());
		getProductDto.setCategoryId(product.getCategoryId());
		getProductDto.setCurrentStock(product.getCurrentStock());
		getProductDto.setMinStockLevel(product.getMinStockLevel());

		return getProductDto;
	}

}
