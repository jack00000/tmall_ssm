package com.edu.tmall.controller;

import com.edu.tmall.pojo.User;
import com.edu.tmall.service.UserService;
import com.edu.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by taffy on 17/11/28.
 */
@Controller
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询用户列表
     * @param page
     * @param model
     * @return
     */
    @RequestMapping("admin_user_list")
    public String list(Page page, Model model) {
        List<User> us = userService.list();
        PageHelper.offsetPage(page.getStart(),page.getCount());
        int total = (int) new PageInfo<>(us).getTotal();
        page.setTotal(total);
        model.addAttribute("us",us);
        model.addAttribute("page",page);

        return "admin/listUser";
    }



}
