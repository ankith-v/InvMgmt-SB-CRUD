package com.example.inventorymgmt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventorymgmt.model.Inventory;
import com.example.inventorymgmt.repository.InventoryRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class InventoryController {

    @Autowired
    InventoryRepository inventoryRepository;

    @GetMapping("/inventory")
    public ResponseEntity<List<Inventory>> getAllItems(@RequestParam(required = false) String title) {
        try {
            List<Inventory> items = new ArrayList<Inventory>();

            if (title == null)
                items.addAll(inventoryRepository.findAll());
            else
                items.addAll(inventoryRepository.findByTitleContaining(title));

            if (items.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(items, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<Inventory> getItemById(@PathVariable("id") long id) {
        Optional<Inventory> inventoryData = inventoryRepository.findById(id);

        if (inventoryData.isPresent()) {
            return new ResponseEntity<>(inventoryData.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/inventory")
    public ResponseEntity<Inventory> createItem(@RequestBody Inventory inventory) {
        try {
            Inventory _inventory = inventoryRepository
                    .save(new Inventory(inventory.getTitle(), inventory.getDescription(), inventory.isPublished()));
            return new ResponseEntity<>(_inventory, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/inventory/{id}")
    public ResponseEntity<Inventory> updateItem(@PathVariable("id") long id, @RequestBody Inventory inventory) {
        Optional<Inventory> inventoryData = inventoryRepository.findById(id);

        if (inventoryData.isPresent()) {
            Inventory _inventory = inventoryData.get();
            _inventory.setTitle(inventory.getTitle());
            _inventory.setDescription(inventory.getDescription());
            _inventory.setPublished(inventory.isPublished());
            return new ResponseEntity<>(inventoryRepository.save(_inventory), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/inventory/{id}")
    public ResponseEntity<HttpStatus> deleteItem(@PathVariable("id") long id) {
        try {
            inventoryRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/inventory")
    public ResponseEntity<HttpStatus> deleteAllItems() {
        try {
            inventoryRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/inventory/published")
    public ResponseEntity<List<Inventory>> findByPublished() {
        try {
            List<Inventory> items = inventoryRepository.findByPublished(true);

            if (items.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(items, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}