package com.example.inventory_management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.inventory_management.dto.InventoryDataDto;
import com.example.inventory_management.dto.ProductsDto;
import com.example.inventory_management.repository.ProductsRepository;
import com.example.inventory_management.response.ResponseInOut;
import com.example.inventory_management.response.StockResponse;

/**
 *
 * @author 芳末 拓也
 *
 *         在庫取得のキャッシュ機能のテスト行うクラス
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CasheServiceTest {

	@Autowired
	InventoryInfomationService inventoryInfomationService;

	@MockitoBean
	InventoryDataService inventoryDataService;

	@MockitoBean
	ProductsRepository productsRepository;

	/**
	 * 在庫取得のキャッシュ機能のテスト
	 *
	 * @throws Exception
	 */
	@Test
	void testCash() throws Exception {

		// 商品IDを用意
		UUID productId = UUID.randomUUID();

		ProductsDto mockProductDto = new ProductsDto();
		mockProductDto.setId(productId);
		mockProductDto.setName("テスト商品");
		mockProductDto.setDescription("テスト商品説明");
		mockProductDto.setPrice(1000);
		mockProductDto.setCategoryId(1);
		mockProductDto.setMinStockLevel(10);
		mockProductDto.setCurrentStock(0L);
		mockProductDto.setCreatedAt(LocalDateTime.now());
		mockProductDto.setLastUpdated(LocalDateTime.now());

		// モックされたProductsRepositoryの戻り値を設定
		when(productsRepository.findById(productId)).thenReturn(Optional.of(mockProductDto));

		// 在庫取得(初回)
		StockResponse firstResponse = inventoryInfomationService.getStockByProductId(productId);
		// 引数で指定された商品IDに対応する在庫数が返却されているか確認
		assertThat(firstResponse.getCurrentStock()).isEqualTo(0L);
		// 在庫取得メソッドが1回呼び出されることを確認
		verify(productsRepository, times(1)).findById(productId);

		// 在庫取得(二回目)
		StockResponse secondResponse = inventoryInfomationService.getStockByProductId(productId);
		// 二回目に取得したデータが初回リクエストと一致することを確認
		assertThat(secondResponse).isEqualTo(firstResponse);
		// 在庫取得メソッドが二回目に呼び出されないことを確認
		verify(productsRepository, times(1)).findById(productId);

		// 入荷情報オブジェクトを作成
		InventoryDataDto arrivalDto = new InventoryDataDto();
		arrivalDto.setProductId(productId);
		arrivalDto.setQuantity(100);
		arrivalDto.setLocationId("warehouse-1");
		arrivalDto.setNote("定期入荷");

		// モックされたinventoryDataServiceの戻り値を設定
		ResponseInOut mockArrivalResponse = new ResponseInOut(UUID.randomUUID(), productId, 100, 100L, "IN",
				LocalDateTime.now());
		when(inventoryDataService.arrival(arrivalDto)).thenReturn(mockArrivalResponse);

		// 初回の在庫情報と入荷後の在庫情報が異なっているか確認
		assertThat(mockArrivalResponse).isNotEqualTo(firstResponse);
		// 入荷数量が反映されているか確認
		assertThat(mockArrivalResponse.getCurrentStock()).isEqualTo(100L);

		// 出荷情報オブジェクトを作成
		InventoryDataDto shipmentDto = new InventoryDataDto();
		shipmentDto.setProductId(productId);
		shipmentDto.setQuantity(50);
		shipmentDto.setLocationId("warehouse-1");
		shipmentDto.setNote("出荷");

		// モックされたinventoryDataServiceの戻り値を設定
		ResponseInOut mockShipmentResponse = new ResponseInOut(UUID.randomUUID(), productId, 50, 50L, "OUT",
				LocalDateTime.now());
		when(inventoryDataService.shipment(shipmentDto)).thenReturn(mockShipmentResponse);

		// 初回の在庫情報と出荷後の在庫情報が異なっているか確認
		assertThat(mockShipmentResponse).isNotEqualTo(firstResponse);
		// 出荷数量が反映されているか確認
		assertThat(mockShipmentResponse.getCurrentStock()).isEqualTo(50L);

	}
}