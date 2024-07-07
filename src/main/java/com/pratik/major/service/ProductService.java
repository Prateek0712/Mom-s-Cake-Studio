package com.pratik.major.service;

import com.pratik.major.repository.ProductRepository;
import com.pratik.major.model.Category;
import com.pratik.major.model.Product;
import com.pratik.major.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts()
    {
        return productRepository.findAll();
    }
    public void deleteProductById(Long id)
    {
        productRepository.deleteById(id);
    }

    public Optional<Product> getProductById(Long id)
    {
        return productRepository.findById(id);
    }

    public List<Product> getAllProductByCategoryId(Integer id)
    {
        Category category=categoryRepository.findById(id).get();
        List<Product> productList=category.getProductList();
        return productList;
    }
    public void addProduct(Product product)
    {
        Category category=product.getCategory();
        category.getProductList().add(product);
        categoryRepository.save(category);
    }

    public void removeProductById(Long id)
    {
        productRepository.deleteById(id);
    }
}
