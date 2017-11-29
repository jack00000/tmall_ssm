package com.edu.tmall.service.impl;

import com.edu.tmall.mapper.ProductMapper;
import com.edu.tmall.pojo.Category;
import com.edu.tmall.pojo.Product;
import com.edu.tmall.pojo.ProductExample;
import com.edu.tmall.pojo.ProductImage;
import com.edu.tmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 何腾飞 on 17/11/27.
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ReviewService reviewService;

    @Override
    public void add(Product p) {
        productMapper.insert(p);
    }

    @Override
    public void delete(int id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Product p) {
        productMapper.updateByPrimaryKeySelective(p);
    }

    @Override
    public Product get(int id) {
        Product p = productMapper.selectByPrimaryKey(id);
        setCategory(p);
        return p;
    }

    public void setCategory(Product p) {
        int cid = p.getCid();
        Category c = categoryService.get(cid);
        p.setCategory(c);
    }

    @Override
    public List list(int cid) {
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria = example.createCriteria();
        criteria.andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        List result = productMapper.selectByExample(example);
        setCategory(result);
        return result;
    }

    @Override
    public void setFirstProductImage(Product p) {
        List<ProductImage> pis = productImageService.list(p.getId(), productImageService.type_single);
        if (!pis.isEmpty()) {
            ProductImage pi = pis.get(0);
            p.setFirstProductImage(pi);
        }
    }

    public void setFirstProductImage(List<Product> ps) {
        for (Product p : ps) {
            setFirstProductImage(p);
        }
    }

    public void setCategory(List<Product> ps) {
        for (Product p : ps) {
            setCategory(p);
        }
    }

    @Override
    public void fill(List<Category> cs) {
        for (Category c : cs) {
            fill(c);
        }
    }

    @Override
    public void fill(Category c) {
        List<Product> ps = list(c.getId());
        c.setProducts(ps);
    }

    /**
     * 为多个分类填充推荐产品集合，即把分类下的产品集合，按照5个为一行，拆成多行，以利于后续页面上进行显示
     * @param cs
     */
    @Override
    public void fillByRow(List<Category> cs) {
        //定义子分类每行显示产品的个数
        int productNumberEachRow = 5;
        for (Category c : cs) {
            List<Product> products = c.getProducts();
            List<List<Product>> productsByRow = new ArrayList<List<Product>>();
            for (int i = 0; i < products.size(); i+=productNumberEachRow) {
                int size = i + productNumberEachRow;
                size = size > products.size()?products.size():size;
                List<Product> productsOfEachRow = products.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            c.setProductsByRow(productsByRow);
        }
    }

    /**
     * 设置销量和评价数量
     * @param product
     */
    @Override
    public void setSaleAndReviewNumber(Product product) {
        //得到订单销量
        int saleCount = orderItemService.getSaleCount(product.getId());
        //得到评价数量
        int count = reviewService.getCount(product.getId());

        product.setSaleCount(saleCount);
        product.setReviewCount(count);

    }

    /**
     * 设置销量和评价数量
     * @param products
     */
    @Override
    public void setSaleAndReviewNumber(List<Product> products) {
        for (Product product : products) {
            setSaleAndReviewNumber(product);
        }
    }
}
