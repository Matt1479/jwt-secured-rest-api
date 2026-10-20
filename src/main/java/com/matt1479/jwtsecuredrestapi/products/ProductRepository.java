package com.matt1479.jwtsecuredrestapi.products;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    
    Optional<Product> findByTitle(String title);

    List<Product> findByDescription(String description);

    boolean existsByTitle(String title);

}
