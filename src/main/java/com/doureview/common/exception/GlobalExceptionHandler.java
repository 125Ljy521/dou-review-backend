package com.doureview.common.exception;

import com.doureview.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕获所有 RuntimeException 异常
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        return Result.fail(e.getMessage());
    }

    // 其他异常捕获（可选）
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        e.printStackTrace(); // 便于开发调试，生产环境可记录日志
        return Result.fail("系统异常，请稍后再试");
    }
}