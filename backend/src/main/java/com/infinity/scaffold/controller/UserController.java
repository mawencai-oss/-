package com.infinity.scaffold.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.infinity.scaffold.common.Result;
import com.infinity.scaffold.entity.User;
import com.infinity.scaffold.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
        String token = userService.login(username, password);
        
        // 获取登录用户信息
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        
        return Result.success("登录成功", result);
    }
    
    /**
     * 获取用户信息
     */
    @ApiOperation("获取用户信息")
    @SaCheckLogin
    @GetMapping("/info")
    public Result<User> getUserInfo() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        return Result.success(user);
    }
    
    /**
     * 用户退出
     */
    @ApiOperation("用户退出")
    @SaCheckLogin
    @PostMapping("/logout")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success();
    }
}
