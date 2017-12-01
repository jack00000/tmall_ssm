package com.edu.tmall.controller;

import com.edu.tmall.pojo.Category;
import com.edu.tmall.service.CategoryService;
import com.edu.tmall.util.ImageUtil;
import com.edu.tmall.util.Page;
import com.edu.tmall.util.UploadImageFile;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by taffy on 17/11/27.
 */
@Controller
@RequestMapping("")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //查询数据列表分页显示
    @RequestMapping("admin_category_list")
    public String list(Model model, Page page) {
        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Category> cs = categoryService.list();
        int total = (int) new PageInfo<>(cs).getTotal();
        page.setTotal(total);
        model.addAttribute("cs",cs);
        model.addAttribute("page",page);
        return "admin/listCategory";
    }

    //添加分类
    @RequestMapping("admin_category_add")
    public String add(Category c, HttpSession session, UploadImageFile uploadImageFile) throws IOException {
        categoryService.add(c);
        File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,c.getId()+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        uploadImageFile.getImage().transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
        return "redirect:/admin_category_list";
    }

    //删除分类
    @RequestMapping("admin_category_delete")
    public String delete(int id,HttpSession session) {
        categoryService.delete(id);
        File  imageFolder= new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
        return "redirect:/admin_category_list";
    }

    //编辑1，查询
    @RequestMapping("admin_category_edit")
    public String get(Model model,int id) throws IOException {
        Category c = categoryService.get(id);
        model.addAttribute("c",c);
        return "admin/editCategory";
    }
    //编辑2，修改
    @RequestMapping("admin_category_update")
    public String update(Category c,HttpSession session,UploadImageFile uploadImageFile) throws IOException {
        categoryService.update(c);
        MultipartFile image = uploadImageFile.getImage();
        if (image!=null && !image.isEmpty()) {
            File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
            File file = new File(imageFolder,c.getId()+".jpg");
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img,"jpg",file);
        }
        return "redirect:/admin_category_list";
    }

}
