package com.example.inventory_management.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.inventory_management.dto.InventoryDataDto;

/**
 *
 * @author 芳末拓也
 *
 *         在庫データを操作するためのリポジトリインターフェース
 *
 */
public interface InventoryDataRepository extends JpaRepository<InventoryDataDto, UUID> {

}