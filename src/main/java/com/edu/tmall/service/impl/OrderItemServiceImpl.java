package com.edu.tmall.service.impl;

import com.edu.tmall.mapper.OrderItemMapper;
import com.edu.tmall.pojo.Order;
import com.edu.tmall.pojo.OrderItem;
import com.edu.tmall.pojo.OrderItemExample;
import com.edu.tmall.pojo.Product;
import com.edu.tmall.service.OrderItemService;
import com.edu.tmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by taffy on 17/11/28.
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ProductService productService;

    @Override
    public void add(OrderItem oi) {
        orderItemMapper.insert(oi);
    }

    @Override
    public void delete(int id) {
        orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderItem oi) {
        orderItemMapper.updateByPrimaryKeySelective(oi);
    }

    @Override
    public OrderItem get(int id) {
        OrderItem oi = orderItemMapper.selectByPrimaryKey(id);
        Product p = productService.get(oi.getPid());
        oi.setProduct(p);
        return oi;
    }

    @Override
    public List<OrderItem> list() {
        OrderItemExample example = new OrderItemExample();
        example.setOrderByClause("id desc");
        return orderItemMapper.selectByExample(example);
    }


    /**
     * 在fill(List<Order> orders) 中，就是遍历每个订单，然后挨个调用fill(Order o)。
     * @param os
     */
    @Override
    public void fill(List<Order> os) {
        for (Order o : os) {
            fill(o);
        }
    }

    /**
     * 根据产品获取销量
     * @param pid
     * @return
     */
    @Override
    public int getSaleCount(int pid) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andPidEqualTo(pid);
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        int result = 0;
        for (OrderItem oi : orderItems) {
            result += oi.getNumber();
        }
        return result;
    }

    /**
     * 通过用户查询订单项列表
     * @param uid
     * @return
     */
    @Override
    public List<OrderItem> listByUser(int uid) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andUidEqualTo(uid).andOidIsNull();
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        for (OrderItem oi : orderItems) {
            Product p = productService.get(oi.getPid());
            oi.setProduct(p);
        }
        return orderItems;
    }
    /**
     * 在订单管理界面，首先是遍历多个订单，然后遍历这个订单下的多个订单项，这里就是做订单与订单项的一对多关系。
     * 在fill(Order o)中：
     1. 根据订单id查询出其对应的所有订单项
     2. 通过setProduct为所有的订单项设置Product属性
     3. 遍历所有的订单项，然后计算出该订单的总金额和总数量
     4. 最后再把订单项设置在订单的orderItems属性上。
     * @param o
     */
    @Override
    public void fill(Order o) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andOidEqualTo(o.getId());
        example.setOrderByClause("id desc");
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);

        for (OrderItem oi: orderItems) {
            Product p = productService.get(oi.getPid());
            oi.setProduct(p);
        }

        //定义商品总金额
        float total = 0;
        //定义商品总数
        int totalNumber = 0;

        for (OrderItem oi:orderItems) {
            total += oi.getNumber()*oi.getProduct().getPromotePrice();
            totalNumber+=oi.getNumber();
        }
        o.setTotal(total);
        o.setTotalNumber(totalNumber);
        o.setOrderItems(orderItems);
    }


}
