package com.edu.tmall.service;

import com.edu.tmall.pojo.Review;

import java.util.List;

/**
 * Created by taffy on 17/11/29.
 */
public interface ReviewService {

    void add(Review review);

    void delete(int id);

    void update(Review review);

    Review get(int id);

    //通过产品获取评价集合
    List<Review> list(int pid);

    //通过产品获取累计评价数
    int getCount(int pid);

}
