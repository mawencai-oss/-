package com.infinity.scaffold.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.infinity.scaffold.entity.Menu;
import com.infinity.scaffold.entity.RoleMenu;
import com.infinity.scaffold.mapper.MenuMapper;
import com.infinity.scaffold.mapper.RoleMenuMapper;
import com.infinity.scaffold.service.IMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单权限Service实现类
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Menu> selectMenuTreeByUserId(Long userId) {
        List<Menu> menus = menuMapper.selectMenusByUserId(userId);
        return buildMenuTree(menus);
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        List<Menu> returnList = new ArrayList<>();
        Map<Integer, Menu> menuMap = new HashMap<>();
        
        // 将菜单列表存入Map，方便后续使用
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);
        }
        
        // 组装父子结构
        for (Menu menu : menus) {
            // 获取父节点
            Menu parentMenu = menuMap.get(menu.getParentId());
            
            if (parentMenu != null) {
                parentMenu.getChildren().add(menu);
            } else {
                // 如果是顶级节点，直接添加到返回列表
                returnList.add(menu);
            }
        }
        
        return returnList;
    }

    @Override
    public List<Integer> selectMenuListByRoleId(Integer roleId) {
        return menuMapper.selectMenusByRoleId(roleId)
                .stream()
                .map(Menu::getId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenusToRole(Integer roleId, List<Integer> menuIds) {
        // 先删除原有的角色菜单关联
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(queryWrapper);
        
        // 添加新的角色菜单关联
        if (menuIds != null && !menuIds.isEmpty()) {
            for (Integer menuId : menuIds) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu);
            }
        }
        
        return true;
    }

    @Override
    public Set<String> getUserPermissions(Long userId) {
        List<Menu> menus = menuMapper.selectMenusByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        
        for (Menu menu : menus) {
            if (menu != null && menu.getPerms() != null && !menu.getPerms().trim().isEmpty()) {
                permsSet.add(menu.getPerms());
            }
        }
        
        return permsSet;
    }
} 