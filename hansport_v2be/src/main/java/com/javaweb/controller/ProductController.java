package com.javaweb.controller;

import com.javaweb.domain.Product;
import com.javaweb.domain.request.ReqProductDTO;
import com.javaweb.domain.response.ResultPaginationDTO;
import com.javaweb.domain.response.product.ResCreateProductDTO;
import com.javaweb.domain.response.product.ResProductDTO;
import com.javaweb.domain.response.product.ResUpdateProductDTO;
import com.javaweb.service.ProductService;
import com.javaweb.util.annotation.ApiMessage;
import com.javaweb.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    @ApiMessage("create a product")
    public ResponseEntity<ResCreateProductDTO> createProduct(@RequestBody @Valid ReqProductDTO product) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.handleSaveProduct(product));
    }

    @PutMapping("/products")
    @ApiMessage("update a product")
    public ResponseEntity<ResUpdateProductDTO> updateProduct(@RequestBody @Valid ReqProductDTO product) throws IdInvalidException {
        if(!this.productService.existsById(product.getId())){
            throw new IdInvalidException("Không có sản phẩm");
        }
        return ResponseEntity.ok().body(this.productService.handleUpdateProduct(product));
    }

    @DeleteMapping("/products/{id}")
    @ApiMessage("delete a products")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) throws IdInvalidException {
        if(!this.productService.existsById(id)){
            throw new IdInvalidException("Không có sản phẩm");
        }
        this.productService.deleteProductById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/{id}")
    @ApiMessage("get product by id")
    public ResponseEntity<ResProductDTO> getProductById(@PathVariable long id) throws IdInvalidException {
        if(!this.productService.existsById(id)){
            throw new IdInvalidException("Không có sản phẩm");
        }

        return ResponseEntity.ok().body(this.productService.fetchProductById(id));
    }

    @GetMapping("/products")
    @ApiMessage("get all products")
    public ResponseEntity<ResultPaginationDTO> getAllProducts(@Filter Specification<Product> spec,
                                                              Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.productService.fetchAllProducts(spec, pageable));
    }


}
