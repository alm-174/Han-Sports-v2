package com.javaweb.service;

import com.javaweb.domain.Product;
import com.javaweb.domain.ProductImage;
import com.javaweb.repository.ProductImageRepository;
import com.javaweb.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class ProductServiceTest {

    @Test
    void replaceImagesShouldRemoveOldImagesAndKeepOnlyRequestedOnes() {
        ProductRepository productRepository = Mockito.mock(ProductRepository.class);
        ProductImageRepository productImageRepository = Mockito.mock(ProductImageRepository.class);
        CloudinaryService cloudinaryService = Mockito.mock(CloudinaryService.class);

        ProductService productService = new ProductService(productRepository, productImageRepository, cloudinaryService);

        Product product = new Product();
        product.setImages(new ArrayList<>());

        ProductImage oldImage = new ProductImage();
        oldImage.setImageUrl("https://old.example.com/old.jpg");
        oldImage.setProduct(product);
        product.getImages().add(oldImage);

        productService.replaceImages(List.of("https://new.example.com/new.jpg"), product);

        assertEquals(1, product.getImages().size());
        assertEquals("https://new.example.com/new.jpg", product.getImages().get(0).getImageUrl());
        verify(productImageRepository).delete(oldImage);
        verify(cloudinaryService).deleteByUrl("https://old.example.com/old.jpg");
    }
}
