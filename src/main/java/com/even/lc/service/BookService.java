package com.even.lc.service;

import com.even.lc.dao.BookDao;
import com.even.lc.dao.CategoryDao;
import com.even.lc.pojo.Book;
import com.even.lc.pojo.Category;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookDao bookDao;

    @Autowired
    private CategoryService categoryService;

    /**
     * 查出所有书籍
     * @return
     */
    public List<Book>list (){
        Sort sort=new Sort(Sort.Direction.DESC,"id");
        return bookDao.findAll(sort);
    }

    /**
     * 当不存在时会保存，存在时会更新
     * @param book
     */
    public void addOrUpdate(Book book){
        bookDao.save(book);
    }

    /**
     * 通过id删除
     * @param id
     */
    public void deleteById(int id){
        bookDao.deleteById(id);
    }

    /**
     * 分类查出所有书籍
     * @param cid
     * @return
     */
    public List<Book> listByCategory(int cid){
        Category category=categoryService.get(cid);
        return bookDao.findAllByCategory(category);
    }
    public List<Book> Search(String keywords) {
        return bookDao.findAllByTitleLikeOrAuthorLike('%' + keywords + '%', '%' + keywords + '%');
    }

}
