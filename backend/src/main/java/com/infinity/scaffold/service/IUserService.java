package com.infinity.scaffold.service;

import com.infinity.scaffold.entity.User;

/**
 * 用户Service接口
 */
public interface IUserService extends IBaseService<User> {

    /**
     * 根据用户名获取用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    User getByUsername(String username);
    
    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    String login(String username, String password);
} 