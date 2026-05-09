package com.javaweb.service;

import com.javaweb.domain.Product;
import com.javaweb.domain.User;
import com.javaweb.domain.request.ReqProductDTO;
import com.javaweb.domain.response.ResultPaginationDTO;
import com.javaweb.domain.response.product.ResCreateProductDTO;
import com.javaweb.domain.response.product.ResProductDTO;
import com.javaweb.domain.response.product.ResUpdateProductDTO;
import com.javaweb.domain.response.user.ResUserDTO;
import com.javaweb.repository.ProductRepository;
import com.javaweb.util.error.IdInvalidException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ResCreateProductDTO handleSaveProduct(ReqProductDTO req) throws IdInvalidException {
        if (this.productRepository.existsByName(req.getName())) {
            throw new IdInvalidException("Sản phẩm đã tồn tại");
        }
        Product product = new Product();
        this.applyProductRequest(product, req);
        Product currentProduct = this.productRepository.save(product);
        return this.convertToResCreateProductDTO(currentProduct);
    }

    public ResUpdateProductDTO handleUpdateProduct(ReqProductDTO product) throws IdInvalidException {
        Product currentProduct = this.productRepository.findById(product.getId())
                .orElseThrow(() -> new IdInvalidException("Không có sản phẩm"));

        Optional<Product> sameName = this.productRepository.findByName(product.getName());
        if (sameName.isPresent() && sameName.get().getId() != currentProduct.getId()) {
            throw new IdInvalidException("Tên sản phẩm đã tồn tại");
        }

        this.applyProductRequest(currentProduct, product);
        this.productRepository.save(currentProduct);
        return convertToResUpdateProductDTO(currentProduct);
    }


    public ResProductDTO fetchProductById(long id) {
        Optional<Product> optionalPr = this.productRepository.findById(id);
        if(optionalPr.isPresent()) {
            return convertToResProductDTO(optionalPr.get());
        }
        return null;
    }

    public void deleteProductById(long id) {
        this.productRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchAllProducts(Specification<Product> spec, Pageable pageable){
        Page<Product> products = this.productRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber()+1);
        meta.setPagesize(pageable.getPageSize());
        meta.setPages(products.getTotalPages());
        meta.setTotal(products.getTotalElements());

        resultPaginationDTO.setMeta(meta);

        List<ResProductDTO> listProduct = products.getContent().
                stream().map(item -> this.convertToResProductDTO(item))
                .collect(Collectors.toList());

        resultPaginationDTO.setResult(listProduct);

        return resultPaginationDTO;
    }
    public boolean existsByName(String name){
        return this.productRepository.existsByName(name);
    }

    public boolean existsById(long id){
        return this.productRepository.existsById(id);
    }

    private void applyProductRequest(Product product, ReqProductDTO req) {
        product.setName(req.getName());
        product.setPrice(req.getPrice());
        product.setShortDesc(req.getShortDesc());
        product.setDetailDesc(req.getDetailDesc());
        product.setBrand(req.getBrand());
        product.setTarget(req.getTarget());
        product.setQuantity(req.getQuantity());
        product.setSold(req.getSold());
        product.setImage(req.getImage());
    }

    public ResCreateProductDTO convertToResCreateProductDTO(Product product) {
        ResCreateProductDTO resCreateProductDTO = new ResCreateProductDTO();
        resCreateProductDTO.setId(product.getId());
        resCreateProductDTO.setName(product.getName());
        resCreateProductDTO.setPrice(product.getPrice());
        resCreateProductDTO.setDetailDesc(product.getDetailDesc());
        resCreateProductDTO.setShortDesc(product.getShortDesc());
        resCreateProductDTO.setQuantity(product.getQuantity());
        resCreateProductDTO.setSold(product.getSold());
        resCreateProductDTO.setTarget(product.getTarget());
        resCreateProductDTO.setBrand(product.getBrand());
        resCreateProductDTO.setCreatedAt(product.getCreatedAt());
        return resCreateProductDTO;
    }

    public ResUpdateProductDTO convertToResUpdateProductDTO(Product product) {
        ResUpdateProductDTO resUpdateProductDTO = new ResUpdateProductDTO();
        resUpdateProductDTO.setId(product.getId());
        resUpdateProductDTO.setName(product.getName());
        resUpdateProductDTO.setPrice(product.getPrice());
        resUpdateProductDTO.setDetailDesc(product.getDetailDesc());
        resUpdateProductDTO.setShortDesc(product.getShortDesc());
        resUpdateProductDTO.setQuantity(product.getQuantity());
        resUpdateProductDTO.setSold(product.getSold());
        resUpdateProductDTO.setTarget(product.getTarget());
        resUpdateProductDTO.setBrand(product.getBrand());
        resUpdateProductDTO.setUpdatedAt(product.getUpdatedAt());
        return resUpdateProductDTO;
    }

    public ResProductDTO convertToResProductDTO(Product product) {
        ResProductDTO resProductDTO = new ResProductDTO();
        resProductDTO.setId(product.getId());
        resProductDTO.setName(product.getName());
        resProductDTO.setPrice(product.getPrice());
        resProductDTO.setDetailDesc(product.getDetailDesc());
        resProductDTO.setShortDesc(product.getShortDesc());
        resProductDTO.setQuantity(product.getQuantity());
        resProductDTO.setSold(product.getSold());
        resProductDTO.setTarget(product.getTarget());
        resProductDTO.setBrand(product.getBrand());
        resProductDTO.setCreatedAt(product.getCreatedAt());
        resProductDTO.setUpdatedAt(product.getUpdatedAt());
        return resProductDTO;
    }
}
