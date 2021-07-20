package com.example.inventorymgmt.repository;

import com.example.inventorymgmt.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByPublished(boolean published);

    List<Inventory> findByTitleContaining(String title);
}
