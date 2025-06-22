package com.infinity.scaffold.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.infinity.scaffold.entity.Role;
import com.infinity.scaffold.entity.UserRole;
import com.infinity.scaffold.mapper.RoleMapper;
import com.infinity.scaffold.mapper.UserRoleMapper;
import com.infinity.scaffold.service.IRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色Service实现类
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public Role getByRoleCode(String roleCode) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, roleCode);
        return getOne(wrapper);
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        // 查询用户角色关联
        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        List<UserRole> userRoles = userRoleMapper.selectList(wrapper);
        
        if (userRoles == null || userRoles.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取角色ID列表
        List<Integer> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        
        // 查询角色列表
        return listByIds(roleIds);
    }
} 