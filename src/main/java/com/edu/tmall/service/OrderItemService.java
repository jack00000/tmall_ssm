package com.edu.tmall.service;

import com.edu.tmall.pojo.Order;
import com.edu.tmall.pojo.OrderItem;

import java.util.List;

/**
 * Created by 何腾飞 on 17/11/28.
 */
public interface OrderItemService {
    void add(OrderItem oi);

    void delete(int id);

    void update(OrderItem oi);

    OrderItem get(int id);

    List<OrderItem> list();

    void fill(Order o);

    void fill(List<Order> os);
}
