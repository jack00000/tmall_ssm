package com.edu.tmall.controller;

import com.edu.tmall.pojo.ProductImage;
import com.edu.tmall.service.ProductImageService;
import com.edu.tmall.service.ProductService;
import com.edu.tmall.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by 何腾飞 on 17/11/28.
 */
@Controller
@RequestMapping("")
public class ProductImageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @RequestMapping("admin_productImage_add")
    public String add(ProductImage pi, HttpSession session, UploadedImageFile uploadedImageFile) {
        productImageService.add(pi);
        String fileName = pi.getId()+".jpg";
        String imageFolder;
        String imageFolder_small = null;
        String imageFolder_middle = null;

        return "redirect:admin_productImage_list?pid="+pi.getPid();
    }
}
