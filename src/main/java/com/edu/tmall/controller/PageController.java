package com.edu.tmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 为了方便访问WEB-INF下面的页面而作。方便服务端访问
 * Created by taffy on 17/11/29.
 */
@Controller
@RequestMapping("")
public class PageController {

    //注册页
    @RequestMapping("registerPage")
    public String registerPage() {
        return "fore/register";
    }

    //注册成功页
    @RequestMapping("registerSuccess")
    public String registerSuccess() {
        return "fore/registerSuccess";
    }

    //登录页
    @RequestMapping("loginPage")
    public String loginPage() {
        return "fore/login";
    }

    //支付
    @RequestMapping("forealipay")
    public String alipay() {
        return "fore/alipay";
    }
}
