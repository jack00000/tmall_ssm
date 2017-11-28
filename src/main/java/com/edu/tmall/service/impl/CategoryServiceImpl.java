package com.edu.tmall.service.impl;

import com.edu.tmall.mapper.CategoryMapper;
import com.edu.tmall.pojo.Category;
import com.edu.tmall.pojo.CategoryExample;
import com.edu.tmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by taffy on 17/11/27.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    //查询列表
    @Override
    public List<Category> list() {
        CategoryExample example = new CategoryExample();
        example.setOrderByClause("id desc");
        return categoryMapper.selectByExample(example);
    }

    @Override
    public void add(Category category) {
        categoryMapper.insert(category);
    }

    @Override
    public void delete(int id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Category get(int id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(Category category) {
        //updateByPrimaryKeySelective，其作用是只修改变化了的字段，未变化的字段就不修改
        categoryMapper.updateByPrimaryKeySelective(category);
    }

}
