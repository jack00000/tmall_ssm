package com.edu.tmall.controller;

import com.edu.tmall.pojo.Category;
import com.edu.tmall.pojo.Property;
import com.edu.tmall.service.CategoryService;
import com.edu.tmall.service.PropertyService;
import com.edu.tmall.util.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by 何腾飞 on 17/11/27.
 */
@Controller
@RequestMapping("")
public class PropertyController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PropertyService propertyService;

    //添加分类属性
    @RequestMapping("admin_property_add")
    public String add(Property p, Model model) {
        propertyService.add(p);
        return "redirect:admin_property_list?cid="+p.getCid();
    }

    //删除分类属性
    @RequestMapping("admin_property_delete")
    public String delete(int id) {
        //根据主键查询属性P对象，并将属性对象的cid传过去
        Property p = propertyService.get(id);
        propertyService.delete(id);
        return "redirect:admin_property_list?cid="+p.getCid();
    }

    //编辑分类属性
    @RequestMapping("admin_property_edit")
    public String edit(int id,Model model) {
        Property p = propertyService.get(id);
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);
        model.addAttribute("p",p);
        return "admin/editProperty";
    }

    //修改分类属性
    @RequestMapping("admin_property_update")
    public String update(Property p) {
        propertyService.update(p);
        return "redirect:admin_property_list?cid="+p.getCid();
    }

    //查询分类属性
    @RequestMapping("admin_property_list")
    public String list(int cid, Model model, Page page) {
        Category c = categoryService.get(cid);

        //设置分页起始位置和每页显示多少记录
        PageHelper.offsetPage(page.getStart(),page.getCount());
        //得到查询数据
        List<Property> ps = propertyService.list(cid);
        //获取属性总数
        int total = (int) new PageInfo<>(ps).getTotal();
        //设置属性的分页总数
        page.setTotal(total);
        //设置分页参数id
        page.setParam("&cid="+c.getId());

        model.addAttribute("ps",ps);
        model.addAttribute("c",c);
        model.addAttribute("page",page);
        return "admin/listProperty";
    }

}
