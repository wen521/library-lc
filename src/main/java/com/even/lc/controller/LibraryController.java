package com.even.lc.controller;

import com.even.lc.pojo.Book;
import com.even.lc.service.BookService;
import com.even.lc.service.CategoryService;
import com.even.lc.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class LibraryController {

    @Autowired
    private BookService bookService;

    /**
     * @CrossOrigin进行前后端跨域
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @GetMapping("/api/books")
    public List<Book> list ()throws Exception{
        return bookService.list();
    }

    @CrossOrigin
    @PostMapping("/api/books")
    public Book addOrUpdate(@RequestBody Book book) throws Exception{
        bookService.addOrUpdate(book);
        return book;
    }

    @CrossOrigin
    @PostMapping("/api/delete")
    public void delete(@RequestBody Book  book) throws Exception{
        bookService.deleteById(book.getId());
    }

    @CrossOrigin
    @GetMapping("/api/categories/{cid}/books")
    public List<Book> listByCategory(@PathVariable("cid") int cid) throws Exception{

        if (0!=cid)
            return bookService.listByCategory(cid);
        return list();
    }

    @CrossOrigin
    @GetMapping("/api/search")
    public List<Book> searchResult(@RequestParam("keywords") String keywords) {
        // 关键词为空时查询出所有书籍
        if ("".equals(keywords)) {
            return bookService.list();
        } else {
            return bookService.Search(keywords);
        }
    }

    /**
     * 前端向后端发送 post 请求，后端对接收到的数据进行处理（压缩、格式转换、重命名等），
     * 并保存到服务器中指定的位置，再把该位置对应的 URL 返回给前端即可。
     * 这里涉及到对文件的操作，对接收到的文件重命名，但保留原始的格式
     * @param file
     * @return
     * @throws Exception
     */
    @CrossOrigin
    @PostMapping("api/covers")
    public String coversUpload(MultipartFile file) throws Exception {
        String folder = "D:/workspace/img";
        File imageFolder = new File(folder);
        File f = new File(imageFolder, StringUtils.getRandomString(6) + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            file.transferTo(f);
            String imgURL = "http://localhost:8443/api/file/" + f.getName();
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
