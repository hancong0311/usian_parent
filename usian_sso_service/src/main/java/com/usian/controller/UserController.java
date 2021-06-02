package com.usian.controller;

import com.usian.pojo.TbUser;
import com.usian.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Title: UserController
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/24 9:38
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("checkUserInfo/{checkValue}/{checkFlag}")
    public boolean checkUserInfo(@PathVariable("checkValue") String checkValue, @PathVariable("checkFlag") Integer checkFlag){
        return  userService.checkUserInfo(checkValue,checkFlag);
    }

    @RequestMapping("/userRegister")
    public void userRegister(@RequestBody TbUser user){
        userService.userRegister(user);
    }

    @RequestMapping("/userLogin")
    public Map userLogin(@RequestParam("username") String username, @RequestParam("password") String password){
        return userService.userLogin(username,password);
    }

    @RequestMapping("/getUserByToken/{token}")
    public boolean getUserByToken(@PathVariable("token") String token){
        return userService.getUserByToken(token);
    }


}
