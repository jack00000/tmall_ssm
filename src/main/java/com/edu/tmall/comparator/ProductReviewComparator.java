package com.edu.tmall.comparator;

import com.edu.tmall.pojo.Product;

import java.util.Comparator;

/**
 * 按评论数量排序
 * Created by taffy on 17/11/30.
 */
public class ProductReviewComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getReviewCount()-p1.getReviewCount();
    }
}
