package com.edu.tmall.controller;

import com.edu.tmall.pojo.*;
import com.edu.tmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 前台的controller
 * Created by taffy on 17/11/29.
 */
@Controller
@RequestMapping("")
public class ForeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ReviewService reviewService;

    /**
     * 访问首页
     * @param model
     * @return
     */
    @RequestMapping("forehome")
    public String home(Model model) {
        //查询首页所有分类数据
        List<Category> cs = categoryService.list();
        productService.fill(cs);
        productService.fillByRow(cs);
        model.addAttribute("cs",cs);
        return "fore/home";
    }

    /**
     * 注册功能
     * @param user
     * @param model
     * @return
     */
    @RequestMapping("foreregister")
    public String register(User user,Model model) {
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);
        if (exist) {
            String m = "用户名已存在";
            model.addAttribute("msg",m);
            model.addAttribute("user",null);
            return "fore/register";
        }
        userService.add(user);
        return "redirect:registerSuccess";
    }

    /**
     * 前台登录
     * @param name
     * @param password
     * @param session
     * @param model
     * @return
     */
    @RequestMapping("forelogin")
    public String login(@RequestParam String name, @RequestParam String password, HttpSession session,Model model) {
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name, password);
        if (user!=null) {
            session.setAttribute("user",user);
            return "redirect:forehome";
        }
        model.addAttribute("msg","账号密码错误");
        return "fore/login";
    }

    /**
     * 退出
     */
    @RequestMapping("forelogout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:forehome";
    }

    /**
     * 产品页面
     * @param pid
     * @param model
     * @return
     */
    @RequestMapping("foreproduct")
    public String product(int pid,Model model) {
        // 根据pid获取product对象
        Product p = productService.get(pid);
        // 获取产品的主图和详情图片集合
        List<ProductImage> productSingleImages = productImageService.list(pid, productImageService.type_single);
        List<ProductImage> productDetailImages = productImageService.list(pid, productImageService.type_detail);
        //将图片分别设置到p对象
        p.setProductSingleImages(productSingleImages);
        p.setProductDetailImages(productDetailImages);

        // 获取该产品的所有属性值
        List<PropertyValue> propertyValues = propertyValueService.list(pid);
        // 获取产品对应的所有的评价
        List<Review> reviews = reviewService.list(pid);

        productService.setSaleAndReviewNumber(p);
        model.addAttribute("p",p);
        model.addAttribute("pvs",propertyValues);
        model.addAttribute("reviews",reviews);
        return "fore/product";
    }

    /**
     * 检查session中是否存在该用户，检查用户是否登录
     * @param session
     * @return
     */
    @RequestMapping("forecheckLogin")
    @ResponseBody
    public String checkLogin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user!=null)
            return "success";
        return "fail";
    }

    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String loginAjax(@RequestParam String name,@RequestParam String password,HttpSession session) {
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name, password);
        if (user!=null) {
            session.setAttribute("user",user);
            return "success";
        }
        return "faile";
    }

}
