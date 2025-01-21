package com.example.inventory_management.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.example.inventory_management.dto.GetProductsDto;
import com.example.inventory_management.dto.ProductsDto;
import com.example.inventory_management.repository.ProductsRepository;
import com.example.inventory_management.response.RegisterResponse;
import com.example.inventory_management.response.UpdateProductResponse;

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

	/**
	 * 商品情報を更新するメソッド
	 *
	 * @param productId 更新対象の商品ID
	 * @param dto       更新する商品情報
	 * @return 更新された商品情報
	 */
	// 商品情報の更新
	public UpdateProductResponse updateProduct(UUID productId, ProductsDto dto) {
		// 商品情報を取得
		ProductsDto product = productsRepository.findById(productId).orElse(null);

		// 商品が見つからない場合
		if (product == null) {
			throw new IllegalArgumentException("指定された商品が見つかりません");
		}

		// 最初に取得したlastUpdatedを保持
		LocalDateTime initialLastUpdated = product.getLastUpdated();

		System.out.println(product.getLastUpdated());
		System.out.println(dto.getLastUpdated());
		// 更新前に競合がないかチェック（現在のlastUpdatedと最初に取得したlastUpdatedを比較）
		if (!initialLastUpdated.equals(dto.getLastUpdated())) {
			throw new OptimisticLockingFailureException("競合が発生しました");
		}

		// 更新処理
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setPrice(dto.getPrice());
		product.setCategoryId(dto.getCategoryId());
		product.setMinStockLevel(dto.getMinStockLevel());
		product.setLastUpdated(LocalDateTime.now());

		// 保存して更新された商品を返す
		ProductsDto updatedProduct = productsRepository.save(product);

		return new UpdateProductResponse(updatedProduct.getId(), updatedProduct.getName(),
				updatedProduct.getDescription(), updatedProduct.getPrice(), updatedProduct.getCategoryId(),
				updatedProduct.getMinStockLevel(), updatedProduct.getLastUpdated());

	}

}
