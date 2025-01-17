package com.example.inventory_management.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory_management.dto.InventoryDataDto;
import com.example.inventory_management.dto.ProductsDto;
import com.example.inventory_management.repository.InventoryDataRepository;
import com.example.inventory_management.repository.ProductsRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionTest {

	@Autowired
	private InventoryDataService inventoryDataService;

	@MockitoBean
	private InventoryDataRepository inventoryDataRepository;

	@MockitoBean
	private InventoryInfomationService inventoryInfomationService;

	@Spy
	private ProductsRepository productsRepository;

	@Test
	@Transactional
	void testShipmentTransaction() {
		// モックデータの準備
		UUID productId = UUID.randomUUID();
		Long initialStock = 10L;

		ProductsDto mockProductDto = new ProductsDto();
		mockProductDto.setId(productId);
		mockProductDto.setName("テスト商品");
		mockProductDto.setDescription("テスト商品説明");
		mockProductDto.setPrice(1000);
		mockProductDto.setCategoryId(1);
		mockProductDto.setMinStockLevel(10);
		mockProductDto.setCurrentStock(initialStock);
		mockProductDto.setCreatedAt(LocalDateTime.now());
		mockProductDto.setLastUpdated(LocalDateTime.now());

		InventoryDataDto mockDto = new InventoryDataDto();
		mockDto.setProductId(productId);
		mockDto.setQuantity(5);
		mockDto.setLocationId("warehouse-1");
		mockDto.setNote("出荷");

		// モックの設定
		Mockito.when(productsRepository.findById(productId)).thenReturn(Optional.of(mockProductDto));
		Mockito.when(inventoryInfomationService.calculateCurrentStock(productId)).thenReturn(initialStock);
		Mockito.doThrow(new RuntimeException("保存に失敗")).when(inventoryDataRepository).save(mockDto);

		// テスト対象のメソッドを実行し、例外発生を確認
		assertThrows(RuntimeException.class, () -> inventoryDataService.shipment(mockDto));

		// 在庫が更新されていないことを確認（ロールバック確認）
		assertEquals(initialStock, mockProductDto.getCurrentStock());

	}
}
