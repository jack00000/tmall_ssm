package com.edu.tmall.controller;

import com.edu.tmall.pojo.Product;
import com.edu.tmall.pojo.PropertyValue;
import com.edu.tmall.service.ProductService;
import com.edu.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by taffy on 17/11/28.
 */
@Controller
@RequestMapping("")
public class PropertyValueController {

    @Autowired
    private PropertyValueService propertyValueService;

    @Autowired
    private ProductService productService;

    /**
     * 编辑属性值
     * @param pid
     * @param model
     * @return
     */
    @RequestMapping("admin_propertyValue_edit")
    public String edit(int pid, Model model) {
        Product p = productService.get(pid);
        propertyValueService.init(p);
        List<PropertyValue> pvs = propertyValueService.list(p.getId());
        model.addAttribute("p",p);
        model.addAttribute("pvs",pvs);
        return "admin/editPropertyValue";
    }

    @RequestMapping("admin_propertyValue_update")
    @ResponseBody
    public String update(PropertyValue pv) {
        propertyValueService.update(pv);
        return "success";
    }

}
