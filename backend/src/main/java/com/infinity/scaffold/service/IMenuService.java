package com.infinity.scaffold.service;

import com.infinity.scaffold.entity.Menu;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限Service接口
 */
public interface IMenuService extends IBaseService<Menu> {

    /**
     * 根据用户ID查询菜单树
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenuTreeByUserId(Long userId);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由菜单列表
     */
    List<Menu> buildMenuTree(List<Menu> menus);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    List<Integer> selectMenuListByRoleId(Integer roleId);

    /**
     * 根据角色ID设置菜单权限
     *
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     * @return 结果
     */
    boolean assignMenusToRole(Integer roleId, List<Integer> menuIds);

    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> getUserPermissions(Long userId);
} 