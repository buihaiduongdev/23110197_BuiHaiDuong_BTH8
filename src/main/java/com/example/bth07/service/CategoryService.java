package com.example.bth07.service;

import com.example.bth07.entity.Category;
import com.example.bth07.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> search(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }
}
