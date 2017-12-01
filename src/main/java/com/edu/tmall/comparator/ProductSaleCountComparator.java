package com.edu.tmall.comparator;

import com.edu.tmall.pojo.Product;

import java.util.Comparator;

/**
 * 按销量排序
 * Created by taffy on 17/11/30.
 */
public class ProductSaleCountComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return p2.getSaleCount()-p1.getSaleCount();
    }
}
