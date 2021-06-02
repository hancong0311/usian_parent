package com.usian.controller;

import com.usian.api.UserFeign;
import com.usian.pojo.TbUser;
import com.usian.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Title: UserController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/24 9:33
 */
@RestController
@RequestMapping("frontend/sso")
public class UserController {

    @Autowired
    private UserFeign userFeign;

    @RequestMapping("/checkUserInfo/{checkValue}/{checkFlag}")
    public Result checkUserInfo(@PathVariable("checkValue") String checkValue, @PathVariable("checkFlag") Integer checkFlag){
       boolean  result = userFeign.checkUserInfo(checkValue,checkFlag);
       if(result){
           return  Result.ok();
       }else{
           return Result.error("注册失败！！！");
       }

    }

    @RequestMapping("userRegister")
    public Result userRegister( TbUser user){
        userFeign.userRegister(user);
        return Result.ok();
    }

    @RequestMapping("userLogin")
    public Result userLogin(@RequestParam("username") String username,@RequestParam("password") String password){

       Map userInfo =  userFeign.userLogin(username,password);

       //同步cookie购物车的数据到redis
       if(userInfo==null){
           Result.error("登录失败！");
       }
       return Result.ok(userInfo);

    }

    /**
     * 校验token是否是有效
     * @param token
     * @return
     */
    @RequestMapping("/getUserByToken/{token}")
    public Result getUserByToken(@PathVariable("token") String token){
        Boolean result = userFeign.getUserByToken(token);
        if(result){
            return Result.ok();
        }
        return Result.error("token过期了!!!");
    }

}
