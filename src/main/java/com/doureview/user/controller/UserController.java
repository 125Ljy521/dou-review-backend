package com.doureview.user.controller;

import com.doureview.common.Result;
import com.doureview.user.entity.User;
import com.doureview.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户模块接口层
 * 只负责接收请求和返回结果，不处理具体业务逻辑
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        return userService.register(user);
    }

    /**
     * 用户登录
     */
    @ApiOperation("用户登陆")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        return userService.login(user);
    }

    /**
     * 退出登录
     */
    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {
        return userService.logout(token);
    }

    /**
     * 获取当前登录用户信息
     */
    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/me")
    public Result<User> getCurrentUser() {
        return userService.getCurrentUser();
    }

    /**
     * 获取所有用户列表
     */
    @ApiOperation("获取所有用户列表")
    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.ok(userService.list());
    }

    /**
     * 更新用户信息
     */
    @ApiOperation("更新用户信息")
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    /**
     * 删除用户
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    /**
     * 根据 ID 查询用户信息
     */
    @ApiOperation("根据 ID 查询用户信息")
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}