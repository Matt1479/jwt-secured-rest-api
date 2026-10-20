package com.matt1479.jwtsecuredrestapi.products;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.matt1479.jwtsecuredrestapi.products.dto.ProductRequest;
import com.matt1479.jwtsecuredrestapi.products.dto.ProductResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@Validated 
@RestController 
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ProductResponse createProduct(@Valid @RequestBody ProductRequest request) {
        return productService.createProduct(
            request.title(), request.description(), request.price());
    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable @Min(1) Long id) {
        return productService.getProductById(id);
    }

    @GetMapping(params = {"title"})
    public ProductResponse getProductByTitle(@RequestParam String title) {
        return productService.getProductByTitle(title);
    }
    
    @GetMapping(params = {"description"})
    public List<ProductResponse> getProductsByDescription(@RequestParam String description) {
        return productService.getProductsByDescription(description);
    }

    @GetMapping(params = {"!title", "!description"})
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ProductResponse updateProductById(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody ProductRequest request) {
        return productService.updateProductById(
            id,
            request.title(),
            request.description(),
            request.price()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductById(@PathVariable @Min(1) Long id) {
        productService.deleteProductById(id);
    }

}
