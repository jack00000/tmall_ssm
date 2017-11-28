package com.edu.tmall.controller;

import com.edu.tmall.pojo.Order;
import com.edu.tmall.service.OrderItemService;
import com.edu.tmall.service.OrderService;
import com.edu.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

/**
 * 因为订单的增加和删除，都是在前台进行的。 所以OrderController提供的是list方法和delivery(发货)方法
 * Created by taffy on 17/11/28.
 */
@Controller
@RequestMapping("")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    /**
     * 查询订单列表
     * @param page
     * @param model
     * @return
     */
    @RequestMapping("admin_order_list")
    public String list(Page page, Model model) {
        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Order> os = orderService.list();

        //获取订单总数并设置在分页对象上
        int total = (int) new PageInfo<>(os).getTotal();
        page.setTotal(total);

        //订单与订单项的一对多关系,为这些订单填充上orderItems信息
        orderItemService.fill(os);

        model.addAttribute("os",os);
        model.addAttribute("page",page);
        //在listOrder.jsp借助c:forEach把订单集合遍历出来,遍历订单的时候，再把当前订单的orderItem订单项集合遍历出来
        return "admin/listOrder";
    }

    /**
     * 发货，发货后修改发货时间，设置发货状态，并更新到数据库
     * @param o
     * @return
     */
    @RequestMapping("admin_order_delivery")
    public String delivery(Order o) {
        //修改发货时间，设置发货状态
        o.setDeliveryDate(new Date());
        o.setStatus(orderService.waitConfirm);

        orderService.update(o);
        return "redirect:admin_order_list";
    }
}
