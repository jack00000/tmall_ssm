package com.edu.tmall.comparator;

import com.edu.tmall.pojo.Product;

import java.util.Comparator;

/**
 * 分类页面，综合排序
 * Created by taffy on 17/11/30.
 */
public class ProductAllComparator implements Comparator<Product> {
    //综合比较
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getSaleCount()*p2.getReviewCount()-p1.getSaleCount()*p1.getReviewCount();
    }


}
