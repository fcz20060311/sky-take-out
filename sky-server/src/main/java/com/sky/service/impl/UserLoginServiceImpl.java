package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserLoginService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service

public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    WeChatProperties weChatProperties;
    @Autowired
    UserMapper userMapper;



    private static String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";

    public User login(UserLoginDTO userLoginDTO){

        //从微信小程序调用接口
        //获取code
        String openid=getopenId(userLoginDTO.getCode());

        //判断openid是否为空
        if(openid==null){
            //为空则抛出异常
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //根据openid查询数据库是否存在该用户
        User user=userMapper.getByopenId(openid);

        //查询用户是否为新用户
        if(user==null){
            user=new User().builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        return user;
    }

    private String getopenId(String code){

        Map<String, String> map=new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type","authorization_code");

        String json= HttpClientUtil.doGet(WX_LOGIN,map);
        JSONObject jsonObject=JSONObject.parseObject(json);

        String openid=jsonObject.getString("openid");
        return openid;
    }
}
