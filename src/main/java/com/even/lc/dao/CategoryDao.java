package com.even.lc.dao;


import com.even.lc.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

// CategoryDAO 不需要额外构造的方法，JPA 提供的默认方法就够用了
public interface CategoryDao extends JpaRepository<Category,Integer>{


}
