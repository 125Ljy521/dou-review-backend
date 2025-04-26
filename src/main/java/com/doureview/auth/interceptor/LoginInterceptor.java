package com.doureview.auth.interceptor;

import com.doureview.auth.util.JwtUtils;
import com.doureview.auth.util.UserHolder;
import com.doureview.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

/**
 * 登录拦截器 - 校验Token & 登录态续签
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String LOGIN_USER_KEY = "LOGIN:TOKEN:";
    private static final Duration TOKEN_TTL = Duration.ofMinutes(30); // 续签时间

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");

        // 无Token
        if (token == null) {
            return true; // 放行，后续接口做 @LoginRequired 校验
        }

        // Token过期
        if (JwtUtils.isTokenExpired(token)) {
            return true;
        }

        Long userId = JwtUtils.getUserId(token);
        if (userId == null) {
            return true;
        }

        // 从Redis获取用户信息
        String userJson = stringRedisTemplate.opsForValue().get(LOGIN_USER_KEY + token);
        if (userJson == null) {
            return true;
        }

        // 反序列化User对象
        User user = new ObjectMapper().readValue(userJson, User.class);
        UserHolder.save(user);

        // 续签：刷新Token有效期
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, TOKEN_TTL);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束清除ThreadLocal
        UserHolder.remove();
    }
}