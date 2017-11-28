package com.edu.tmall.service.impl;

import com.edu.tmall.mapper.OrderMapper;
import com.edu.tmall.pojo.Order;
import com.edu.tmall.pojo.OrderExample;
import com.edu.tmall.pojo.User;
import com.edu.tmall.service.OrderService;
import com.edu.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by taffy on 17/11/28.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserService userService;

    @Override
    public void add(Order o) {
        orderMapper.insert(o);
    }

    @Override
    public void delete(int id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Order o) {
        orderMapper.updateByPrimaryKeySelective(o);
    }

    @Override
    public Order get(int id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    /**
     * 此处设置了用户与订单的一对多关系，
     * @return
     */
    @Override
    public List<Order> list() {
        OrderExample example = new OrderExample();
        example.setOrderByClause("id desc");
        List<Order> orders = orderMapper.selectByExample(example);
        for (Order o : orders) {
            Integer uid = o.getUid();
            User user = userService.get(uid);
            o.setUser(user);
        }
        return orders;
    }
}
