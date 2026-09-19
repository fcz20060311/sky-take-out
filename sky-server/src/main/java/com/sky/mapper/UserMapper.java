package com.sky.mapper;

import com.sky.entity.User;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {


    User getByopenId(String openid);

    void insert(User user);
}
