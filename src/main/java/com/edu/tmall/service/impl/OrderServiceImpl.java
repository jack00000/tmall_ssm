package com.edu.tmall.service.impl;

import com.edu.tmall.mapper.OrderMapper;
import com.edu.tmall.pojo.Order;
import com.edu.tmall.pojo.OrderExample;
import com.edu.tmall.pojo.OrderItem;
import com.edu.tmall.pojo.User;
import com.edu.tmall.service.OrderItemService;
import com.edu.tmall.service.OrderService;
import com.edu.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private OrderItemService orderItemService;

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

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackForClassName = "Exception")
    public float add(Order o, List<OrderItem> ois) {
        float total = 0;
        add(o);
        if (false) {
            throw new RuntimeException();
        }
        for (OrderItem oi : ois) {
            oi.setOid(o.getId());
            orderItemService.update(oi);
            total += oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        return total;
    }


    @Override
    public List<Order> list() {
        OrderExample example = new OrderExample();
        example.setOrderByClause("id desc");
        return orderMapper.selectByExample(example);
    }


    /**
     * 根据用户Id和状态查询订单
     * @param uid
     * @param excludedStatus
     * @return
     */
    @Override
    public List<Order> list(int uid, String excludedStatus) {
        OrderExample example = new OrderExample();
        example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
        example.setOrderByClause("id desc");
        return orderMapper.selectByExample(example);
    }

    public void setUser(List<Order> os){
        for (Order o : os)
            setUser(o);
    }
    public void setUser(Order o){
        int uid = o.getUid();
        User u = userService.get(uid);
        o.setUser(u);
    }
}
