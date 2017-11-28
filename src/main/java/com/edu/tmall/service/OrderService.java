package com.edu.tmall.service;

import com.edu.tmall.pojo.Order;

import java.util.List;

/**
 * Created by 何腾飞 on 17/11/28.
 */
public interface OrderService {

    //订单状态的常量值
    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";

    void add(Order o);

    void delete(int id);

    void update(Order o);

    Order get(int id);

    List<Order> list();
}
