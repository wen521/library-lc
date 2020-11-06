package com.even.lc.controller;

import com.even.lc.entity.Book;
import com.even.lc.result.Result;
import com.even.lc.result.ResultFactory;
import com.even.lc.service.BookService;
import com.even.lc.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
    public Result listBooks(){
        return ResultFactory.buildSuccessFactory(bookService.list());
    }

    @CrossOrigin
    @PostMapping("/api/admin/content/books")
    public Result addOrUpdateBooks(@RequestBody @Valid Book book) throws Exception{
        bookService.addOrUpdate(book);
        return ResultFactory.buildSuccessFactory("修改成功");
    }

    @CrossOrigin
    @PostMapping("/api/admin/content/books/delete")
    public Result deleteBook(@RequestBody @Valid Book  book) throws Exception{
        bookService.deleteById(book.getId());
        return ResultFactory.buildSuccessFactory("删除成功");
    }

    @CrossOrigin
    @GetMapping("/api/categories/{cid}/books")
    public Result listByCategory(@PathVariable("cid") int cid) throws Exception{

        if (0!=cid) {
            return ResultFactory.buildSuccessFactory(bookService.listByCategory(cid));
        }else {
        return ResultFactory.buildSuccessFactory(bookService.list());
        }
    }

    @CrossOrigin
    @GetMapping("/api/search")
    public Result searchResult(@RequestParam("keywords") String keywords) {
        // 关键词为空时查询出所有书籍
        if ("".equals(keywords)) {
            return ResultFactory.buildSuccessFactory(bookService.list());
        } else {
            return ResultFactory.buildSuccessFactory(bookService.Search(keywords));
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
    @PostMapping("api/admin/content/books/covers")
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
