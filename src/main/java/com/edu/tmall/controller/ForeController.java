package com.edu.tmall.controller;

import com.edu.tmall.comparator.*;
import com.edu.tmall.pojo.*;
import com.edu.tmall.service.*;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

        /**
         * 使用ajax登录 模态登录
         * @param name
         * @param password
         * @param session
         * @return
         */
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

        /**
         * 分类页面功能  使用的是提供5个比较器来进行排序
         * @param cid
         * @param sort
         * @param model
         * @return
         */
        @RequestMapping("forecategory")
        public String category(int cid,String sort,Model model) {
            Category c = categoryService.get(cid);
            productService.fill(c);
            productService.setSaleAndReviewNumber(c.getProducts());

            if (sort!=null) {
                switch (sort) {
                    case "all":
                        Collections.sort(c.getProducts(),new ProductAllComparator());
                        break;
                    case "price":
                        Collections.sort(c.getProducts(),new ProductPriceComparator());
                        break;
                    case "date":
                        Collections.sort(c.getProducts(),new ProductDateComparator());
                        break;
                    case "review":
                        Collections.sort(c.getProducts(),new ProductReviewComparator());
                        break;
                    case "saleCount":
                        Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                        break;
                }
            }
            model.addAttribute("c",c);
            return "fore/category";
        }

        /**
         * 关键字搜索产品功能，并进行分页显示
         * @param keyword
         * @param model
         * @return
         */
        @RequestMapping("foresearch")
        public String search(String keyword,Model model) {
            PageHelper.offsetPage(0,20);
            List<Product> ps = productService.search(keyword);
            productService.setSaleAndReviewNumber(ps);
            model.addAttribute("ps",ps);
            return "fore/searchResult";
        }

        /**
         * 立即购买
         * @param pid
         * @param num
         * @param session
         * @return
         */
        @RequestMapping("forebuyone")
        public String buyone(int pid,int num,HttpSession session) {
            Product p = productService.get(pid);

            int oiid = 0;
            //在session域中查询是否存在此用户
            User user = (User) session.getAttribute("user");
            boolean found = false;
            //根据用户id查询它的订单项列表
            List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
            for (OrderItem oi : orderItems) {
                //判断用户购物车里是否已经存在订单项，如果存在则直接添加数量
                if (oi.getProduct().getId().intValue() == p.getId().intValue()) {
                    oi.setNumber(oi.getNumber()+num);
                    orderItemService.update(oi);
                    found = true;
                    oiid = oi.getId();
                    break;
                }
            }
            //如果用户购物车没有该订单项，则新建一个
            if (!found) {
                OrderItem oi = new OrderItem();
                oi.setUid(user.getId());
                oi.setNumber(num);
                oi.setPid(pid);
                orderItemService.add(oi);
                oiid = oi.getId();
            }
            return "redirect:forebuy?oiid="+oiid;
        }

        /**
         * 由于结算页面还需要显示在购物车中选中的多条OrderItem数据，所以为了兼容从购物车页面跳转过来的需求，要用字符串数组获取多个oiid
         * @param oiid
         * @param session
         * @param model
         * @return
         */
        @RequestMapping("forebuy")
        public String buy(String[] oiid,HttpSession session,Model model) {
            List<OrderItem> ois = new ArrayList<>();
            //定义一个总价格变量
            float total = 0;
            for (String strid : oiid) {
                int id = Integer.parseInt(strid);
                OrderItem oi = orderItemService.get(id);
                total += oi.getProduct().getPromotePrice()*oi.getNumber();
                ois.add(oi);
            }
            session.setAttribute("ois",ois);
            model.addAttribute("total",total);
            return "fore/buy";
        }

        /**
         * 加入购物车功能
         *   1. 获取参数pid
         2. 获取参数num
         3. 根据pid获取产品对象p
         4. 从session中获取用户对象user

         新增订单项要考虑两个情况
         a. 如果已经存在这个产品对应的OrderItem，并且还没有生成订单，即还在购物车中。 那么就应该在对应的OrderItem基础上，调整数量
         a.1 基于用户对象user，查询没有生成订单的订单项集合
         a.2 遍历这个集合
         a.3 如果产品是一样的话，就进行数量追加
         a.4 获取这个订单项的 id

         b. 如果不存在对应的OrderItem,那么就新增一个订单项OrderItem
         b.1 生成新的订单项
         b.2 设置数量，用户和产品
         b.3 插入到数据库
         b.4 获取这个订单项的 id
         * @param pid
         * @param num
         * @param session
         * @return
         */
        @RequestMapping("foreaddCart")
        @ResponseBody
        public String addCart(int pid,int num,HttpSession session) {
            Product p = productService.get(pid);
            User user = (User) session.getAttribute("user");
            boolean found = false;
            List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
            for (OrderItem oi : orderItems) {
                if (oi.getProduct().getId().intValue() == p.getId().intValue()) {
                    oi.setNumber(oi.getNumber()+num);
                    orderItemService.update(oi);
                    found = true;
                    break;
                }
            }
            if (!found) {
                OrderItem oi = new OrderItem();
                oi.setNumber(num);
                oi.setPid(pid);
                oi.setUid(user.getId());
                orderItemService.add(oi);
            }
            return "success";
        }

        /**
         * 跳转到购物车
         * @param session
         * @param model
         * @return
         */
        @RequestMapping("forecart")
        public String cart(HttpSession session,Model model) {
            User user = (User) session.getAttribute("user");
            List<OrderItem> ois = orderItemService.listByUser(user.getId());
            model.addAttribute("ois",ois);
            return "fore/cart";
        }

        /**
         * Ajax异步修改购物车数据，调整订单数量
         * @return
         */
        @RequestMapping("forechangeOrderItem")
        @ResponseBody
        public String changeOrderItem(HttpSession session,int pid,int number) {
            //判断用户是否登录
            User user = (User) session.getAttribute("user");
            if (user==null)
                return "fail";
            //根据用户id取出该用户的所有订单项
            List<OrderItem> orderItems = orderItemService.listByUser(user.getId());
            //遍历订单项
            for (OrderItem oi : orderItems) {
                //判断该用户的产品id是否与传递过来的pid一致
                if (oi.getProduct().getId().intValue() == pid) {
                    oi.setNumber(number);
                    orderItemService.update(oi);
                    break;
                }
            }
            return "success";
        }

        /**
         * 删除购物车的订单项
         * @param session
         * @param oiid
         * @return
         */
        @RequestMapping("foredeleteOrderItem")
        @ResponseBody
        public String deleteOrderItem(HttpSession session,int oiid) {
            User user = (User) session.getAttribute("user");
            if (user==null)
                return "fail";
            orderItemService.delete(oiid);
            return "success";
        }

        /**
     * 创建订单，生成订单
     * 提交订单访问路径 /forecreateOrder, 导致ForeController.createOrder 方法被调用
     1. 从session中获取user对象
     2. 通过参数Order接受地址，邮编，收货人，用户留言等信息
     3. 根据当前时间加上一个4位随机数生成订单号
     4. 根据上述参数，创建订单对象
     5. 把订单状态设置为等待支付
     6. 从session中获取订单项集合 ( 在结算功能的ForeController.buy() 13行，订单项集合被放到了session中 )
     7. 把订单加入到数据库，并且遍历订单项集合，设置每个订单项的order，更新到数据库
     8. 统计本次订单的总金额
     9. 客户端跳转到确认支付页forealipay，并带上订单id和总金额
     */
    @RequestMapping("forecreateOrder")
    public String createOrder(HttpSession session,Order order) {
        User user = (User) session.getAttribute("user");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setUid(user.getId());
        order.setCreateDate(new Date());
        order.setStatus(OrderService.waitPay);

        List<OrderItem> ois = (List<OrderItem>) session.getAttribute("ois");
        float total = orderService.add(order,ois);

        return "redirect:forealipay?oid="+order.getId()+"&total="+total;
    }

    /**
     * 支付成功
     * @param oid
     * @param total
     * @param model
     * @return
     */
    @RequestMapping("forepayed")
    public String payed(int oid,float total,Model model) {
        Order o = orderService.get(oid);
        o.setStatus(OrderService.waitDelivery);
        o.setPayDate(new Date());
        o.setTotal(total);
        orderService.update(o);
        model.addAttribute("o",o);
        return "fore/payed";
    }

    /**
     * 已购买
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("forebought")
    public String bought(Model model,HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<Order> os = orderService.list(user.getId(), OrderService.delete);

        orderItemService.fill(os);
        model.addAttribute("os",os);
        return "fore/bought";
    }

        /**
         * 确认收货
         * @param oid
         * @param model
         * @return
         */
    @RequestMapping("foreconfirmPay")
    public String confirmPay(int oid,Model model) {
        //通过oid获取订单对象o
        Order o = orderService.get(oid);
        //为订单对象填充订单项
        orderItemService.fill(o);
        //把订单对象放在request的属性"o"上
        model.addAttribute("o",o);
        //服务端跳转到 confirmPay.jsp
        return "fore/confirmPay";
    }

        /**
         * 确认支付
         * @param oid
         * @return
         */
    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed(int oid) {
        //根据参数oid获取Order对象o
        Order o = orderService.get(oid);
        //修改对象o的状态为等待评价，修改其确认支付时间
        o.setStatus(OrderService.waitReview);
        o.setConfirmDate(new Date());
        //更新到数据库
        orderService.update(o);
        //服务端跳转到orderConfirmed.jsp页面
        return "fore/orderConfirmed";
    }

        /**
         * 页面点击删除，通过Ajax异步访问删除的方法
         * @param oid
         * @return
         */
    @RequestMapping("foredeleteOrder")
    @ResponseBody
    public String deleteOrder(int oid) {
        //根据oid获取订单对象o
        Order o = orderService.get(oid);
        //修改状态
        o.setStatus(OrderService.delete);
        //更新到数据库
        orderService.update(o);
        //返回字符串"success"
        return "success";
    }

        /**
         * 评价产品页面
         * @param oid
         * @param model
         * @return
         */
    @RequestMapping("forereview")
    public String review(int oid,Model model) {
        //根据oid获取订单对象o
        Order o = orderService.get(oid);

        //为订单对象填充订单项
        orderItemService.fill(o);

        //获取第一个订单项对应的产品,因为在评价页面需要显示一个产品图片，那么就使用这第一个产品的图片了
        Product p = o.getOrderItems().get(0).getProduct();

        //获取这个产品的评价集合
        List<Review> reviewList = reviewService.list(p.getId());

        //为产品设置评价数量和销量
        productService.setSaleAndReviewNumber(p);

        //把产品，订单和评价集合放在request上
        model.addAttribute("p",p);
        model.addAttribute("o",o);
        model.addAttribute("reviews",reviewList);
        //服务端跳转到 review.jsp
        return "fore/review";
    }

    @RequestMapping("foredoreview")
    public String doreview(@RequestParam("oid") int oid,@RequestParam("pid") int pid,String content,HttpSession session) {
        //1.1 获取参数oid
        //1.2 根据oid获取订单对象o
        Order o = orderService.get(oid);

        //1.3 修改订单对象状态
        o.setStatus(OrderService.finish);

        //1.4 更新订单对象到数据库
        orderService.update(o);

        //1.5 获取参数pid
        //1.6 根据pid获取产品对象
        Product p = productService.get(pid);

        //1.7 获取参数content (评价信息)
        //1.8 对评价信息进行转义，道理同注册ForeController.register()
        content = HtmlUtils.htmlEscape(content);

        //1.9 从session中获取当前用户
        User user = (User) session.getAttribute("user");

        //1.10 创建评价对象review
        Review review = new Review();
        //1.11 为评价对象review设置 评价信息，产品，时间，用户
        review.setContent(content);
        review.setPid(pid);
        review.setCreateDate(new Date());
        review.setUid(user.getId());
        //1.12 增加到数据库
        reviewService.add(review);
        //1.13.客户端跳转到/forereview： 评价产品页面，并带上参数showonly=true
        return "redirect:forereview?oid="+oid+"&showonly=true";

    }

}
