package com.example.inventory_management.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory_management.dto.ProductsDto;

/**
 *
 * @author 芳末拓也
 *
 *         商品データを操作するためのリポジトリインターフェース
 *
 */
public interface ProductsRepository extends JpaRepository<ProductsDto, UUID> {

}