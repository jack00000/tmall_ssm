package com.edu.tmall.service.impl;

import com.edu.tmall.mapper.ReviewMapper;
import com.edu.tmall.pojo.Review;
import com.edu.tmall.pojo.ReviewExample;
import com.edu.tmall.pojo.User;
import com.edu.tmall.service.ReviewService;
import com.edu.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by taffy on 17/11/29.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private UserService userService;

    @Override
    public void add(Review review) {
        reviewMapper.insert(review);
    }

    @Override
    public void delete(int id) {
        reviewMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Review review) {
        reviewMapper.updateByPrimaryKeySelective(review);
    }

    @Override
    public Review get(int id) {
        return reviewMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据产品查询对应产品的评价信息集合
     * @param pid
     * @return
     */
    @Override
    public List<Review> list(int pid) {
        ReviewExample example = new ReviewExample();
        example.createCriteria().andPidEqualTo(pid);
        example.setOrderByClause("id desc");
        List<Review> reviews = reviewMapper.selectByExample(example);
        for (Review review : reviews) {
            //根据评价对象的uid属性得到uid
            Integer uid = review.getUid();
            //根据uid得到用户对象
            User user = userService.get(uid);
            //将用户对象设置到review的User属性中
            review.setUser(user);
        }
        return reviews;
    }

    //获得该产品的累计评价
    @Override
    public int getCount(int pid) {
        return list(pid).size();
    }
}
