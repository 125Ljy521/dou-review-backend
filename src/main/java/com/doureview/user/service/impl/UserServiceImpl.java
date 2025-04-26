package com.doureview.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doureview.auth.util.JwtUtils;
import com.doureview.auth.util.UserHolder;
import com.doureview.common.Result;
import com.doureview.user.dto.UserLoginDTO;
import com.doureview.user.dto.UserRegisterDTO;
import com.doureview.user.dto.UserResponseDTO;
import com.doureview.user.entity.User;
import com.doureview.user.mapper.UserMapper;
import com.doureview.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Result<String> register(UserRegisterDTO dto) {
        String username = dto.getUsername();
        String lockKey = "LOCK:REGISTER:" + username;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            if (!lock.tryLock(3, 10, TimeUnit.SECONDS)) {
                return Result.fail("注册请求频繁，请稍后再试");
            }
            boolean exists = lambdaQuery().eq(User::getUsername, username).exists();
            if (exists) {
                return Result.fail("用户名已存在");
            }
            User user = new User();
            BeanUtils.copyProperties(dto, user);
            save(user);
            return Result.success("注册成功");
        } catch (InterruptedException e) {
            return Result.fail("系统异常");
        } finally {
            if (lock.isHeldByCurrentThread()) lock.unlock();
        }
    }

    @Override
    public Result<Map<String, Object>> login(UserLoginDTO dto) {
        User dbUser = lambdaQuery()
                .eq(User::getUsername, dto.getUsername())
                .eq(User::getPassword, dto.getPassword())
                .one();
        if (dbUser == null) {
            return Result.fail("用户名或密码错误");
        }
        String token = JwtUtils.createToken(dbUser.getId());
        try {
            redisTemplate.opsForValue().set("LOGIN:TOKEN:" + token,
                    objectMapper.writeValueAsString(dbUser));
        } catch (Exception e) {
            return Result.fail("登录失败");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", dbUser);
        return Result.success(map);
    }

    @Override
    public Result<String> logout(String token) {
        redisTemplate.delete("LOGIN:TOKEN:" + token);
        return Result.success("退出成功");
    }

    @Override
    public Result<UserResponseDTO> getCurrentUser() {
        User user = UserHolder.get();
        if (user == null) {
            return Result.fail("未登录");
        }
        UserResponseDTO dto = new UserResponseDTO();
        BeanUtils.copyProperties(user, dto);
        return Result.success(dto);
    }

    @Override
    public Result<String> updateUser(Long id, UserRegisterDTO dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setId(id);
        updateById(user);
        return Result.success("更新成功");
    }

    @Override
    public Result<String> deleteUser(Long id) {
        removeById(id);
        return Result.success("删除成功");
    }

    @Override
    public Result<UserResponseDTO> getUser(Long id) {
        User user = getById(id);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        UserResponseDTO dto = new UserResponseDTO();
        BeanUtils.copyProperties(user, dto);
        return Result.success(dto);
    }

    @Override
    public Result<List<UserResponseDTO>> listAllUsers() {
        List<User> users = list();
        List<UserResponseDTO> dtos = users.stream().map(user -> {
            UserResponseDTO dto = new UserResponseDTO();
            BeanUtils.copyProperties(user, dto);
            return dto;
        }).collect(Collectors.toList());
        return Result.success(dtos);
    }
}