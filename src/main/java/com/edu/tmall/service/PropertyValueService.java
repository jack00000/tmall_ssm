package com.edu.tmall.service;

import com.edu.tmall.pojo.Product;
import com.edu.tmall.pojo.PropertyValue;

import java.util.List;

/**
 * Created by 何腾飞 on 17/11/28.
 */
public interface PropertyValueService {

    //初始化PropertyValue
    void init(Product p);

    void update(PropertyValue pv);

    //根据属性id和产品id获取PropertyValue对象
    PropertyValue get(int ptid,int pid);

    //根据产品id获取所有的属性值
    List<PropertyValue> list(int pid);
}
