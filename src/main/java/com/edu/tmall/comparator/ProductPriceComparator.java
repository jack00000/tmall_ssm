package com.edu.tmall.comparator;

import com.edu.tmall.pojo.Product;

import java.util.Comparator;

/**
 * 按价格排序
 * Created by taffy on 17/11/30.
 */
public class ProductPriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return (int) (p1.getPromotePrice()-p2.getPromotePrice());
    }
}
