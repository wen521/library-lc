package com.even.lc.controller;

import com.even.lc.entity.User;
import com.even.lc.result.Result;
import com.even.lc.result.ResultFactory;
import com.even.lc.service.UserService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;


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
        username=HtmlUtils.htmlEscape(username);// 转化

        Subject subject= SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken(username, requestUser.getPassword());
        token.setRememberMe(true);
        try{
            subject.login(token);
            User user=userService.findByUsername(username);
            if (!user.isEnabled())
                return ResultFactory.buildFailFactory("用户已被禁用");

            return ResultFactory.buildSuccessFactory(username);

        }catch (IncorrectCredentialsException e){
            return ResultFactory.buildFailFactory("密码错误");
        }catch (UnknownAccountException e){
            return ResultFactory.buildFailFactory("账号不存在");
        }

    }

    /**
     * 使用shiro后的注册代码
     * @param user
     * @return
     */
    @CrossOrigin
    @PostMapping("/api/register")
    @ResponseBody
    public Result register(@RequestBody User user){
        int status = userService.register(user);
        switch (status) {
            case 0:
                return ResultFactory.buildFailFactory("用户名和密码不能为空");
            case 1:
                return ResultFactory.buildSuccessFactory("注册成功");
            case 2:
                return ResultFactory.buildFailFactory("用户已存在");
        }

        return ResultFactory.buildFailFactory("未知错误");
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
        return ResultFactory.buildSuccessFactory("成功登出");
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
