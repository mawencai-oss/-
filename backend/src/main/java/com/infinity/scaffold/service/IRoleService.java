package com.infinity.scaffold.service;

import com.infinity.scaffold.entity.Role;

import java.util.List;

/**
 * 角色Service接口
 */
public interface IRoleService extends IBaseService<Role> {

    /**
     * 根据角色编码获取角色
     *
     * @param roleCode 角色编码
     * @return 角色对象
     */
    Role getByRoleCode(String roleCode);
    
    /**
     * 获取用户角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getRolesByUserId(Long userId);
} 