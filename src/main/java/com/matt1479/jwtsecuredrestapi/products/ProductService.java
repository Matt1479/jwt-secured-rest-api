package com.matt1479.jwtsecuredrestapi.products;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.matt1479.jwtsecuredrestapi.products.dto.ProductResponse;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse createProduct(
            String title, String description, Double price) {
        if (productRepository.existsByTitle(title)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Product with the title '%s' already exists"
                    .formatted(title)
            );
        }

        Product product = new Product(title, description, price);

        // TODO: handle race condition
        Product savedProduct = productRepository.save(product);
        return toResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product not found"));

        return toResponse(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductByTitle(String title) {
        Product product = productRepository.findByTitle(title)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product not found"));

        return toResponse(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductByDescription(String description) {
        Product product = productRepository.findByDescription(description)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product not found"));

        return toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> products = new ArrayList<>();

        productRepository.findAll()
            .forEach((product) -> products.add(toResponse(product)));
        
        return products;
    }

    @Transactional 
    public ProductResponse updateProduct(
            Long id, String title, String description, Double price) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product not found"));
        
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);

        Product updatedProduct = productRepository.save(product);
        return toResponse(updatedProduct);
    }

    @Transactional 
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Product not found");
        }

        productRepository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getTitle(),
            product.getDescription(),
            product.getPrice()
        );
    }

}
