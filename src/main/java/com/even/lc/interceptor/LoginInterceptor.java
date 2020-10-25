package com.even.lc.interceptor;

import com.even.lc.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 判断Session中的user是否存在，如果存在就放行，不存在就返回login
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // options方法用来查询针对请求URI指定的资源支持的方法。
        // 放行 options 请求，否则无法让前端带上自定义的 header 信息，导致 sessionID 改变，shiro 验证失败
        if (HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value()); //状态码
            return true;
        }

        Subject subject = SecurityUtils.getSubject();
        // 使用 shiro 验证
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return false;
        }
        System.out.println("是否remember："+subject.isRemembered());
        System.out.println("否已通过身份验证(已登录)："+subject.isAuthenticated());
        return true;
    }

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session=request.getSession();
//        String contextPath=session.getServletContext().getContextPath();
//        String[] requireAuthPages=new String[]{
//                "index",
//        };
//
//        String uri=request.getRequestURI();
//
//        uri= StringUtils.remove(uri,contextPath+"/");
//        String page=uri;
//        if (beginPage(page,requireAuthPages)){
//            User user= (User) session.getAttribute("user");
//            if (user==null){
//                //如果Session中没有这个user，会被重定向到 login页面
//                response.sendRedirect("login");
//                return false;
//            }
//
//        }
//        return true;
//    }

    /**
     * 路径列表 requireAuthPages，写下要拦截页面的路径
     * @param page
     * @param requireAuthPages
     * @return
     */
    private boolean beginPage(String page,String[]requireAuthPages){
        boolean result=false;
        for (String requireAuthPage:requireAuthPages){
            if (StringUtils.startsWith(page,requireAuthPage)){
                result=true;
                break;
            }
        }
        return result;
    }
}
