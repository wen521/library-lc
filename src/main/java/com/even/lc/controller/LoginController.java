package com.even.lc.controller;

import com.even.lc.pojo.User;
import com.even.lc.result.Result;
import com.even.lc.result.ResultFactory;
import com.even.lc.service.UserService;

import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;


@Controller

public class LoginController {
    @Autowired
    private UserService userService;

    /**
     * shiro验证登录代码
     * @param requestUser
     * @return
     */
    @CrossOrigin
    @PostMapping(value = "/api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser){
        String username=requestUser.getUsername();
        Subject subject= SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken(username, requestUser.getPassword());
        token.setRememberMe(true);
        try{
            subject.login(token);
            return ResultFactory.buildSuccessFactory(username);
        }catch (AuthenticationException e){
            e.printStackTrace();
            String message="账号或者密码错误";
            return ResultFactory.buildFailFactory(message);
        }
    }
    //使用shiro前的登录代码
//    @CrossOrigin   //前后端跨域
//    @PostMapping(value = "api/login")
//    @ResponseBody
//    public Result login(@RequestBody User requestUser, HttpSession session){
//        String username=requestUser.getUsername();
//        username= HtmlUtils.htmlEscape(username);
//
//        User user =userService.get(username, requestUser.getPassword());//连接数据库更改后的代码
//
//        if (null==user){
//            return new Result(400);
//        }else {
//            //用户信息存在 Session 对象中（当用户在应用程序的 Web 页之间跳转时，存储在 Session 对象中的变量不会丢失）
//            session.setAttribute("user",user);
//            return new Result(200);
//        }
//    }

        //连接数据库更改前的判断代码
//        if (!Objects.equals("admin",username)||!Objects.equals("123456",requestUser.getPassword())){
//            String message="账号或密码错误";
//            System.out.println("test");
//            return new Result(400);
//        }else {
//            return new Result(200);
//        }


    /**
     * 使用shiro后的注册代码
     * @param user
     * @return
     */
    @CrossOrigin
    @PostMapping("/api/register")
    @ResponseBody
    public Result register(@RequestBody User user){

        String username=user.getUsername();
        String password=user.getPassword();
        username=HtmlUtils.htmlEscape(username);//HtmlUtils 把sql代码进行编码转义为html语言
        user.setUsername(username);

        boolean exist=userService.isExit(username);
        if (exist){
            String message="用户名已被占用";
            return ResultFactory.buildFailFactory(message);
        }
        //生成盐   先生成了随机的 byte 数组，又转换成了字符串类型的 base64 编码并返回
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        //设置迭代次数
        int times=2;
        //得到Hash加盐密码
        String encodePassword=new SimpleHash("md5",password,salt,times).toString();
        //存储用户信息 包括salt和hash后的代码
        user.setSalt(salt);
        user.setPassword(encodePassword);
        userService.add(user);
        return ResultFactory.buildSuccessFactory(user);

    }

    /**
     * logout 登出
     * @return buildSuccessFactory(message)
     */
    @CrossOrigin
    @ResponseBody
    @GetMapping("api/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String message = "成功登出";
        return ResultFactory.buildSuccessFactory(message);
    }

    /**
     * 即访问每个页面前都向后端发送一个请求，目的是经由拦截器验证服务器端的登录状态
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @GetMapping(value = "api/authentication")
    public String authentication(){
        return "身份认证成功";
    }

}
