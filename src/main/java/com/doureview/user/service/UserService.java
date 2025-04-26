package com.doureview.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doureview.common.Result;
import com.doureview.user.dto.UserLoginDTO;
import com.doureview.user.dto.UserRegisterDTO;
import com.doureview.user.dto.UserResponseDTO;
import com.doureview.user.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {

    Result<String> register(UserRegisterDTO dto);

    Result<Map<String, Object>> login(UserLoginDTO dto);

    Result<String> logout(String token);

    Result<UserResponseDTO> getCurrentUser();

    Result<String> updateUser(Long id, UserRegisterDTO dto);

    Result<String> deleteUser(Long id);

    Result<UserResponseDTO> getUser(Long id);

    Result<List<UserResponseDTO>> listAllUsers();
}