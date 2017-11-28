package com.edu.tmall.service;

import com.edu.tmall.pojo.ProductImage;

import java.util.List;

/**
 * Created by 何腾飞 on 17/11/28.
 */
public interface ProductImageService {

    //常量 用来表示单张图片
    String type_single = "type_single";
    //常量，表示详情页图片
    String type_detail = "type_detail";

    void add(ProductImage pi);

    void delete(int id);

    void update(ProductImage pi);

    ProductImage get(int id);

    List<ProductImage> list(int pid,String type);
}
