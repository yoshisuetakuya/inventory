package com.example.inventory_management.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory_management.dto.GetProductsDto;

/**
 *
 * @author 芳末拓也
 *
 *         商品情報を取得するためのリポジトリインターフェース
 *
 */
public interface GetProductsRepository extends JpaRepository<GetProductsDto, UUID> {

}