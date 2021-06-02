package com.usian.interceptor;

import com.usian.api.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Title: LoginInterceptor
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/28 8:36
 */
//@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserFeign userFeign;
    /**
     *  请求访问拦截目标方法前执行
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
            // 判断用户是否登录过
        // 从request对象中获取 用户登录过的痕迹              token
        String token = request.getHeader("token");

        final String[] cookieToken = new String[1];
        if(token ==null || token.equals("")){
             token = request.getParameter("token");
             if(token == null || token.equals("")){
                 Cookie[] cookies = request.getCookies();

                 Arrays.asList(cookies).forEach(cookie -> {
                     if(cookie.getName().equals("token")){
                             cookieToken[0] = cookie.getValue();
                     }
                 });
                 token = cookieToken[0];

             }
        }

        // 用户没有携带token
        if(token== null || token.equals("")){
            return false;
        }

        //用户携带token，验证有效
        boolean result = userFeign.getUserByToken(token);
        if(result){
            return true;
        }
        return false;


        // 解析token
        //成功了    //登录过放行  true

        //否则   //没有登录过拦截  false
    }
}
