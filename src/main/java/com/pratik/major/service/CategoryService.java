package com.pratik.major.service;

import com.pratik.major.repository.CategoryRepository;
import com.pratik.major.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public void  addCategory(Category category)
    {
        categoryRepository.save(category);
    }
    public List<Category> getAllCategory()
    {
        return categoryRepository.findAll();
    }

    public void removeCategoryById(Integer id)
    {
        categoryRepository.deleteById(id);
    }

    public Optional<Category> findCategoryById(Integer id)
    {
        return categoryRepository.findById(id);
    }
}
