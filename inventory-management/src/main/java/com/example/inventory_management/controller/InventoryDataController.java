package com.example.inventory_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory_management.dto.InventoryDataDto;
import com.example.inventory_management.response.ResponseInOut;
import com.example.inventory_management.service.InventoryDataService;

@RestController
public class InventoryDataController {

	@Autowired
	private InventoryDataService inventoryDataService;

	// 入荷API
	@PostMapping("/api/v1/inventory/in")
	public ResponseEntity<ResponseInOut> arrive(@RequestBody InventoryDataDto dto) {
		ResponseInOut arrival = inventoryDataService.arrival(dto);
		return ResponseEntity.status(HttpStatus.OK).body(arrival);

	}

	// 出荷API
	@PostMapping("/api/v1/inventory/out")
	public ResponseEntity<ResponseInOut> ship(@RequestBody InventoryDataDto dto) {
		ResponseInOut shipping = inventoryDataService.shipment(dto);
		return ResponseEntity.status(HttpStatus.OK).body(shipping);
	}
}
