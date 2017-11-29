package com.edu.tmall.service;

import com.edu.tmall.pojo.User;

import java.util.List;

/**
 * Created by 何腾飞 on 17/11/28.
 */
public interface UserService {

    void add(User u);

    void delete(int id);

    void update(User u);

    User get(int id);

    List<User> list();

    //判断用户名是否存在
    boolean isExist(String name);

    //根据账号密码查询用户
    User get(String name,String password);
}
