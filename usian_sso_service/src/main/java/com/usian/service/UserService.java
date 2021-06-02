package com.usian.service;

import com.usian.mapper.TbUserMapper;
import com.usian.pojo.TbUser;
import com.usian.util.JwtUtils;
import com.usian.util.MD5Utils;
import com.usian.util.RedisClient;
import com.usian.util.StringUtils;
import org.apache.commons.configuration.AbstractConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Title: UserService
 * @Description:
 * @Auther:
 * @Version: 1.0
 * @create 2021/5/24 9:39
 */
@Service
public class UserService {

    @Autowired
    private TbUserMapper userMapper;

    @Autowired
    private RedisClient redisClient;


    /**
     *
     * @param checkValue
     * @param checkFlag  1:查询用户名   2：查询手机号
     * @return
     */
    public boolean checkUserInfo(String checkValue, Integer checkFlag) {
        TbUser tbUser = new TbUser(); // 查询条件对象
        if(checkFlag==1){//用户名   // 1   select * from tb_user where username = xxx
            tbUser.setUsername(checkValue);
        }else{
            tbUser.setPhone(checkValue);
        }
        List<TbUser> users = userMapper.select(tbUser);
        if(users==null || users.size()==0){
            return true;
        }
        return false;

        //2   select * from tb_user where phone = xxx
    }

    public void userRegister(TbUser user) {

        AbstractConfiguration s;
//        ConfigurationEnvironmentConfiguration C;
        user.setPassword(MD5Utils.digest(user.getPassword()));
        user.setCreated(new Date());
        userMapper.insertSelective(user);
    }

    public Map userLogin(String username, String password) {
        HashMap map = new HashMap<>();

        //1. 去数据库做对比  select * from user where username = xxx and passwword = xxx
        TbUser tbUser = new TbUser(); // 查询条件对象
        tbUser.setUsername(username);
//        tbUser.setPassword(MD5Utils.digest(password));
        List<TbUser> users = userMapper.select(tbUser);
        //2. 对比成功，留痕迹
        if(users!=null && users.size()>0){
            if(users.get(0).getPassword().equals(MD5Utils.digest(password))){
                //3. 存redis   什么结构来存？String   存啥  用户信息----》token
                //  key:token   value:用户
//                String token = UUID.randomUUID().toString();
//                redisClient.set(token,users.get(0));
//                redisClient.expire(token,60*60*24);
                // JWT 规范生成一个token
                // 获取私钥
                String privateKey = (String)redisClient.get("PRIVATE_KEY");
               byte[] privateKeyArr =  StringUtils.toByteArray(privateKey);
                try {
                   String token =  JwtUtils.generateToken(users.get(0),privateKeyArr,60*30);
                    map.put("token",token);
                    map.put("userid",users.get(0).getId());
                    map.put("username",users.get(0).getUsername());
                    map.put("flag",true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //4. 存cookie??? 返回浏览器需要的信息，前端自己存入到cookie

                return map;
            }else{
                map.put("flag",false);
                map.put("msg","密码输入错误！");
            }
        }else{// 用户名输入错误
            map.put("flag",false);
            map.put("msg","用户名输入错误！");
        }
        return null;



    }

    /**
     * 校验token是否是有效
     * @param token
     * @return
     */
    public boolean getUserByToken(String token) {
        String publicKey = (String) redisClient.get("PUBLIC_KEY");
        try {
            TbUser tbUser = JwtUtils.getInfoFromToken(token, StringUtils.toByteArray( publicKey));
            if(tbUser==null){
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
//        Object user = redisClient.get(token);
//        if (user==null){
//            return false;
//        }
//        return true;

    }
}
