package com.doureview.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doureview.auth.util.JwtUtils;
import com.doureview.auth.util.UserHolder;
import com.doureview.common.Result;
import com.doureview.user.entity.User;
import com.doureview.user.mapper.UserMapper;
import com.doureview.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户模块业务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 用户注册功能，使用 Redisson 分布式锁防止重复提交
     */
    @Override
    public Result<String> register(User user) {
        String username = user.getUsername();
        // 分布式锁 key
        String lockKey = "LOCK:REGISTER:" + username;
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 尝试获取锁，最多等待3秒，锁定10秒
            if (!lock.tryLock(3, 10, TimeUnit.SECONDS)) {
                return Result.fail("注册请求频繁，请稍后再试");
            }

            // 查询用户名是否已存在
            boolean exists = lambdaQuery()
                    .eq(User::getUsername, user.getUsername())
                    .exists();
            if (exists) {
                return Result.fail("用户名已存在");
            }

            // 保存用户
            save(user);
            return Result.ok("注册成功");

        } catch (InterruptedException e) {
            return Result.fail("系统异常，请稍后重试");
        } finally {
            // 释放锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 用户登录功能：验证用户名密码，生成 token 并保存至 Redis
     */
    @Override
    public Result<Map<String, Object>> login(User user) {
        // 根据用户名和密码查询数据库
        User dbUser = lambdaQuery()
                .eq(User::getUsername, user.getUsername())
                .eq(User::getPassword, user.getPassword())
                .one();
        if (dbUser == null) {
            return Result.fail("用户名或密码错误");
        }

        // 生成 JWT token
        String token = JwtUtils.createToken(dbUser.getId());

        // 保存用户信息到 Redis
        String key = "LOGIN:TOKEN:" + token;
        try {
            stringRedisTemplate.opsForValue().set(key, new ObjectMapper().writeValueAsString(dbUser));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("登录失败");
        }

        // 返回 token 和用户信息
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", dbUser);
        return Result.ok(result);
    }

    /**
     * 退出登录：删除 Redis 中的 token
     */
    @Override
    public Result<String> logout(String token) {
        stringRedisTemplate.delete("LOGIN:TOKEN:" + token);
        return Result.ok("退出登录成功");
    }

    /**
     * 获取当前登录用户信息
     */
    @Override
    public Result<User> getCurrentUser() {
        User loginUser = UserHolder.get();
        if (loginUser == null) {
            return Result.fail("未登录");
        }
        return Result.ok(loginUser);
    }

    /**
     * 更新用户信息
     */
    @Override
    public Result<String> updateUser(Long id, User user) {
        user.setId(id);
        updateById(user);
        return Result.ok("更新成功");
    }

    /**
     * 删除用户
     */
    @Override
    public Result<String> deleteUser(Long id) {
        removeById(id);
        return Result.ok("删除成功");
    }

    /**
     * 根据 ID 查询用户信息
     */
    @Override
    public Result<User> getUser(Long id) {
        User user = getById(id);
        return Result.ok(user);
    }
}