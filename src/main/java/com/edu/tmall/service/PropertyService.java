package com.edu.tmall.service;

import com.edu.tmall.pojo.Property;

import java.util.List;

/**
 * Created by 何腾飞 on 17/11/27.
 */
public interface PropertyService {
    void add(Property p);

    void delete(int id);

    void update(Property p);

    Property get(int id);

    List list(int cid);
}
