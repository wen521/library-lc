package com.even.lc.service;

import com.even.lc.dao.BookDao;
import com.even.lc.entity.Book;
import com.even.lc.entity.Category;
import com.even.lc.redis.RedisService;
import com.even.lc.util.CastUtils;
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

    @Autowired
    private RedisService redisService;

    /**
     * 查出所有书籍
     * @return
     */
    public List<Book>list (){
        List<Book> books;
        String key = "bookList";
        Object bookCache = redisService.get(key);

        if (bookCache == null){
            Sort sort = new Sort(Sort.Direction.DESC,"id"); //按id倒序排列
            books = bookDao.findAll(sort);
            redisService.set(key,books);
        }else {
            books = CastUtils.objectConvertToList(bookCache,Book.class);
        }
        return books;
    }

    /**
     * 当不存在时会保存，存在时会更新
     * @param book
     */
    public void addOrUpdate(Book book){
        redisService.delete("booklist");
        bookDao.save(book);
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        redisService.delete("booklist");
    }

    /**
     * 通过id删除
     * @param id
     */
    public void deleteById(int id){
        redisService.delete("booklist");
        bookDao.deleteById(id);
        try{
            Thread.sleep(500);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        redisService.delete("booklist");
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
