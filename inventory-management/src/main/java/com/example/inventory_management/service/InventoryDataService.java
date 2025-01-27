package com.example.inventory_management.service;

import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory_management.dto.InventoryDataDto;
import com.example.inventory_management.dto.ProductsDto;
import com.example.inventory_management.repository.InventoryDataRepository;
import com.example.inventory_management.repository.ProductsRepository;
import com.example.inventory_management.response.ResponseInOut;

/**
 *
 * @author 芳末拓也
 *
 *         商品の入荷および出荷処理を行うサービスクラス
 *
 */
@Service
public class InventoryDataService {

	@Autowired
	private InventoryDataRepository inventoryDataRepository;

	@Autowired
	private ProductsRepository productsRepository;

	@Autowired
	private InventoryInfomationService inventoryInfomationService;

	/**
	 * 商品入荷処理メソッド
	 *
	 * @param dto
	 * @return 入荷処理結果レスポンス
	 */
	@CacheEvict(value = "stockCache", key = "#p0.productId")
	public ResponseInOut arrival(InventoryDataDto dto) {
		// 商品情報を取得
		final ProductsDto product = productsRepository.findById(dto.getProductId()).orElse(null);

		if (product == null) {
			throw new IllegalArgumentException("商品が見つかりません");
		}

		// 在庫を計算
		Long currentStock = inventoryInfomationService.calculateCurrentStock(dto.getProductId());

		// 在庫数を更新
		currentStock += dto.getQuantity();
		product.setCurrentStock(currentStock);
		productsRepository.save(product);

		// 入荷のトランザクションタイプを保存
		dto.setTransactionType("IN");

		// 保存
		final InventoryDataDto savedDto = inventoryDataRepository.save(dto);

		return new ResponseInOut(savedDto.getTransactionId(), savedDto.getProductId(), savedDto.getQuantity(),
				currentStock, "IN", LocalDateTime.now());
	}

	/**
	 * 商品出荷処理メソッド
	 *
	 * @param dto
	 * @return 出荷処理結果レスポンス
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@CacheEvict(value = "stockCache", key = "#p0.productId")
	public ResponseInOut shipment(InventoryDataDto dto) {
		// 商品情報を取得
		final ProductsDto product = productsRepository.findById(dto.getProductId()).orElse(null);

		if (product == null) {
			throw new IllegalArgumentException("商品が見つかりません");
		}

		// 在庫を計算
		Long currentStock = inventoryInfomationService.calculateCurrentStock(dto.getProductId());

		// 在庫数が不足していた場合
		if (currentStock < dto.getQuantity()) {
			throw new IllegalArgumentException("在庫が不足しています");
		}

		// 在庫数を更新
		currentStock -= dto.getQuantity();
		product.setCurrentStock(currentStock);
		productsRepository.save(product);

		// 出荷のトランザクションタイプを保存
		dto.setTransactionType("OUT");
		// 保存
		final InventoryDataDto savedDto = inventoryDataRepository.save(dto);

		return new ResponseInOut(savedDto.getTransactionId(), savedDto.getProductId(), savedDto.getQuantity(),
				currentStock, "OUT", LocalDateTime.now());
	}
}