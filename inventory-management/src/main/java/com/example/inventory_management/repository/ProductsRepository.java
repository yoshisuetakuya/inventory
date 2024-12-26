package com.example.inventory_management.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory_management.dto.ProductsDto;

public interface ProductsRepository extends JpaRepository<ProductsDto, UUID> {

}