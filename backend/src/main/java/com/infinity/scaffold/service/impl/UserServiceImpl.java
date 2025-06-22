package com.infinity.scaffold.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.infinity.scaffold.entity.User;
import com.infinity.scaffold.exception.BusinessException;
import com.infinity.scaffold.mapper.UserMapper;
import com.infinity.scaffold.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    public String login(String username, String password) {
        // 根据用户名查询用户
        User user = getByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 密码验证
        if (!SaSecureUtil.sha256(password).equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 判断用户状态
        if (user.getStatus() != 0) {
            throw new BusinessException("用户已被禁用");
        }
        
        // 登录并返回token
        StpUtil.login(user.getId());
        return StpUtil.getTokenValue();
    }
} 