package com.tonyeapp.estore.storeapp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ProductRepository extends JpaRepository<Product, Long> {
    // Page<Product> findByDescriptionContainingIgnoreCase(List<Product> findByDescription(String description);)
    Page<Product> findByDescriptionContainingIgnoreCase(String description, Pageable pageable);

}
