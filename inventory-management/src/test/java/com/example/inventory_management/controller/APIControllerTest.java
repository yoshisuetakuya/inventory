package com.example.inventory_management.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;

import com.example.inventory_management.dto.InventoryDataDto;
import com.example.inventory_management.dto.ProductsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class APIControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	// 登録、取得、照会、入荷、出荷、在庫数を取得、期間内の取引情報の取得まで一連の流れでテストする
	void APITest() throws Exception {

		// 1.商品登録APIのテスト
		ProductsDto product = new ProductsDto();
		product.setName("テスト商品");
		product.setDescription("テスト商品説明");
		product.setPrice(1000);
		product.setCategoryId(1);
		product.setMinStockLevel(10);

		// JSONに変換
		String productJson = objectMapper.writeValueAsString(product);

		MvcResult result = mockMvc
				.perform(post("/api/v1/products").contentType(MediaType.APPLICATION_JSON).content(productJson))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.name").value("テスト商品")).andExpect(jsonPath("$.description").value("テスト商品説明"))
				.andExpect(jsonPath("$.price").value(1000)).andExpect(jsonPath("$.categoryId").value(1))
				.andExpect(jsonPath("$.minStockLevel").value(10)).andExpect(jsonPath("$.createdAt").isNotEmpty())
				.andReturn();// ステータスが201で返ることとレスポンスが仕様通り返ってきていること

		// レスポンスから商品IDを抽出
		String response = result.getResponse().getContentAsString();
		UUID productId = UUID.fromString(JsonPath.read(response, "$.id"));

		// 2.商品取得APIのテスト
		mockMvc.perform(get("/api/v1/products/" + productId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(productId.toString())).andExpect(jsonPath("$.name").value("テスト商品"))
				.andExpect(jsonPath("$.description").value("テスト商品説明")).andExpect(jsonPath("$.price").value(1000))
				.andExpect(jsonPath("$.categoryId").value(1)).andExpect(jsonPath("$.currentStock").value(0))
				.andExpect(jsonPath("$.minStockLevel").value(10));// ステータスが200で返ることとレスポンスが仕様通り返ってきていること

		// 3.入荷APIのテスト
		InventoryDataDto inventoryIn = new InventoryDataDto();
		inventoryIn.setProductId(productId);
		inventoryIn.setQuantity(100);
		inventoryIn.setLocationId("warehouse-1");
		inventoryIn.setNote("定期入荷");

		// JSONに変換
		String inventoryInJson = objectMapper.writeValueAsString(inventoryIn);

		mockMvc.perform(post("/api/v1/inventory/in").contentType(MediaType.APPLICATION_JSON).content(inventoryInJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.transactionId").isNotEmpty())
				.andExpect(jsonPath("$.productId").value(productId.toString()))
				.andExpect(jsonPath("$.quantity").value(100)).andExpect(jsonPath("$.currentStock").value(100))
				.andExpect(jsonPath("$.transactionType").value("IN")).andExpect(jsonPath("$.timestamp").isNotEmpty());// ステータスが200で返ることとレスポンスが仕様通り返ってきていること

		// 4.出荷APIのテスト
		InventoryDataDto inventoryOut = new InventoryDataDto();
		inventoryOut.setProductId(productId);
		inventoryOut.setQuantity(50);
		inventoryOut.setLocationId("warehouse-1");
		inventoryOut.setNote("出荷");

		// JSONに変換
		String inventoryOutJson = objectMapper.writeValueAsString(inventoryOut);

		mockMvc.perform(post("/api/v1/inventory/out").contentType(MediaType.APPLICATION_JSON).content(inventoryOutJson))
				.andExpect(status().isOk()).andExpect(jsonPath("$.transactionId").isNotEmpty())
				.andExpect(jsonPath("$.productId").value(productId.toString()))
				.andExpect(jsonPath("$.quantity").value(50)).andExpect(jsonPath("$.currentStock").value(50))
				.andExpect(jsonPath("$.transactionType").value("OUT")).andExpect(jsonPath("$.timestamp").isNotEmpty());// ステータスが200で返ることとレスポンスが仕様通り返ってきていること

		// 5.在庫照会APIのテスト
		mockMvc.perform(get("/api/v1/inventory/stock?productId=" + productId)).andExpect(status().isOk())
				.andExpect(jsonPath("$.productId").value(productId.toString()))
				.andExpect(jsonPath("$.currentStock").value(50)).andExpect(jsonPath("$.minStockLevel").value(10))
				.andExpect(jsonPath("$.lastUpdated").isNotEmpty());// ステータスが200で返ることとレスポンスが仕様通り返ってきていること

		// 6.期間内の取引情報を取得するAPIのテスト
		String fromDate = "2024-01-01T00:00:00"; // 任意の過去の日付
		String toDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); // 現在の日付

		mockMvc.perform(get("/api/v1/inventory/transactions?productId=" + productId + "&fromDate=" + fromDate
				+ "&toDate=" + toDate)).andExpect(status().isOk()).andExpect(jsonPath("$.transactions").isArray())
				.andExpect(jsonPath("$.transactions[0].transactionId").isNotEmpty())
				.andExpect(jsonPath("$.transactions[0].productId").value(productId.toString()))
				.andExpect(jsonPath("$.transactions[0].quantity").value(100))
				.andExpect(jsonPath("$.transactions[0].transactionType").value("IN"))
				.andExpect(jsonPath("$.transactions[0].note").value("定期入荷"))
				.andExpect(jsonPath("$.transactions[0].timestamp").isNotEmpty())
				.andExpect(jsonPath("$.totalCount").value(2));// ステータスが200で返ることとレスポンスが仕様通り返ってきていること

	}

}
