package com.doureview.user.controller;

import com.doureview.common.Result;
import com.doureview.user.dto.UserLoginDTO;
import com.doureview.user.dto.UserRegisterDTO;
import com.doureview.user.dto.UserResponseDTO;
import com.doureview.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<String> register(@RequestBody UserRegisterDTO registerDTO) {
        return userService.register(registerDTO);
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody UserLoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {
        return userService.logout(token);
    }

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/me")
    public Result<UserResponseDTO> getCurrentUser() {
        return userService.getCurrentUser();
    }

    @ApiOperation("获取所有用户列表")
    @GetMapping("/list")
    public Result<List<UserResponseDTO>> list() {
        return userService.listAllUsers();
    }

    @ApiOperation("更新用户信息")
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable Long id, @RequestBody UserRegisterDTO dto) {
        return userService.updateUser(id, dto);
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @ApiOperation("根据 ID 查询用户信息")
    @GetMapping("/{id}")
    public Result<UserResponseDTO> getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}