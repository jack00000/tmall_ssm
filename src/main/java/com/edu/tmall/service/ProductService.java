package com.edu.tmall.service;

import com.edu.tmall.pojo.Category;
import com.edu.tmall.pojo.Product;

import java.util.List;

/**
 * Created by 何腾飞 on 17/11/27.
 */
public interface ProductService {

    void add(Product p);

    void delete(int id);

    void update(Product p);

    Product get(int id);

    List list(int cid);

    void setFirstProductImage(Product p);

    void fill(List<Category> cs);

    void fill(Category c);

    void fillByRow(List<Category> cs);

    void setSaleAndReviewNumber(Product product);

    void setSaleAndReviewNumber(List<Product> products);
}
