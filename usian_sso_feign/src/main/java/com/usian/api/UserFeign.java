package com.usian.api;

import com.usian.pojo.TbUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Title: UserFeign
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/24 9:37
 */
@FeignClient("usian-sso-service")
public interface UserFeign {

    @RequestMapping("/user/getUserByToken/{token}")
    public boolean getUserByToken(@PathVariable("token") String token);
    @RequestMapping("/user/checkUserInfo/{checkValue}/{checkFlag}")
    public boolean checkUserInfo(@PathVariable("checkValue") String checkValue, @PathVariable("checkFlag") Integer checkFlag);

    @RequestMapping("user/userRegister")
    public void userRegister(@RequestBody TbUser user);

    @RequestMapping("user/userLogin")
    public Map userLogin(@RequestParam("username") String username, @RequestParam("password") String password);

}
