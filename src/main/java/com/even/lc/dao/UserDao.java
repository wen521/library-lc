package com.even.lc.dao;

import com.even.lc.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    User findByUsername(String name);

    User getByUsernameAndPassword(String username,String password);
}
