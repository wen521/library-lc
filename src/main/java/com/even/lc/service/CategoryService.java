package com.even.lc.service;

import com.even.lc.dao.CategoryDao;
import com.even.lc.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    /**
     * 分类查找，并按照id倒序排列
     * @return
     */
    public List<Category> list(){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        return categoryDao.findAll(sort);
    }

    /**
     * 判断是否存在
     * @param id
     * @return
     */
    public Category get(int id){
        Category category=categoryDao.findById(id).orElse(null);
        return category;
    }
}
