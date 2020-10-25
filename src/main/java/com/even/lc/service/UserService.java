package com.even.lc.service;

import com.even.lc.dao.UserDao;
import com.even.lc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 判断用户是否存在ByName
     * @param username
     * @return
     */
    public boolean isExit(String username){
        User user=getByName(username);
        return null!=user;

    }

    /**
     * 通过username获取用户信息
     * @param username
     * @return
     */
    public User getByName(String username) {
       return userDao.findByUsername(username);
    }

    /**
     * 通过username，password获取用户
     * @param username
     * @param password
     * @return
     */
    public User get(String username,String password){
        return userDao.getByUsernameAndPassword(username,password);
    }

    /**
     * 直接使用JPa方法
     * @param user
     */
    public void add(User user){
        userDao.save(user);
    }

}
