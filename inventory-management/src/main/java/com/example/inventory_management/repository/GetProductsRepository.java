package com.example.inventory_management.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inventory_management.dto.GetProductsDto;

public interface GetProductsRepository extends JpaRepository<GetProductsDto, UUID> {

}