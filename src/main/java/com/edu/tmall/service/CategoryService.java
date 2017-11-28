package com.edu.tmall.service;

import com.edu.tmall.pojo.Category;

import java.util.List;

/**
 * Created by taffy on 17/11/27.
 */
public interface CategoryService {

    List<Category> list();

    void add(Category category);

    void delete(int id);

    Category get(int id);

    void update(Category category);
}
