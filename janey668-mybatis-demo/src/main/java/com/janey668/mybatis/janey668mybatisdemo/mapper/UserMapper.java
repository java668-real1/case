package com.janey668.mybatis.janey668mybatisdemo.mapper;

import com.janey668.mybatis.janey668mybatisdemo.entity.SysUser;

import java.util.List;

/**
 * @author Jerry.chen
 * @desc
 * @date 2022/07/01 10:48
 **/
public interface UserMapper {
    List<SysUser> selectByIdList(List<Long> asList);
}