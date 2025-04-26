package com.doureview.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doureview.common.Result;
import com.doureview.user.entity.User;

import java.util.Map;

/**
 * 用户模块业务层接口
 */
public interface UserService extends IService<User> {

    Result<String> register(User user);

    Result<Map<String, Object>> login(User user);

    Result<String> logout(String token);

    Result<User> getCurrentUser();

    Result<String> updateUser(Long id, User user);

    Result<String> deleteUser(Long id);

    Result<User> getUser(Long id);
}