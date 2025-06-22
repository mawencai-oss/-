package com.infinity.scaffold.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果类
 *
 * @param <T> 数据类型
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功
     */
    public static <T> Result<T> success(T data) {
        return success("操作成功", data);
    }

    /**
     * 成功
     */
    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * 失败
     */
    public static <T> Result<T> error() {
        return error("操作失败");
    }

    /**
     * 失败
     */
    public static <T> Result<T> error(String msg) {
        return error(500, msg);
    }

    /**
     * 失败
     */
    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
} 