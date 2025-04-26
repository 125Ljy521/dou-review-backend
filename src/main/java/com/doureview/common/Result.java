package com.doureview.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    // 成功（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 成功（不带数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    // 失败（默认500）
    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message, null);
    }

    // 失败（自定义错误码）
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}