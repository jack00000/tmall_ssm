package com.edu.tmall.comparator;

import com.edu.tmall.pojo.Product;

import java.util.Comparator;

/**
 * 按日期排序
 * Created by taffy on 17/11/30.
 */
public class ProductDateComparator implements Comparator<Product>{


    @Override
    public int compare(Product p1, Product p2) {
        return p1.getCreateDate().compareTo(p2.getCreateDate());
    }
}
