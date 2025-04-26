package com.doureview.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private T data;

    // 成功返回（带数据）
    public static <T> Result<T> ok(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 成功返回（提示语）
    public static Result<String> okMessage(String message) {
        return new Result<>(200, message, null);
    }

    // 失败返回（泛型版）
    public static <T> Result<T> fail(String message) {
        return new Result<>(400, message, null);
    }
}