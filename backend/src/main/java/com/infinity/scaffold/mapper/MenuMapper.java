package com.infinity.scaffold.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.infinity.scaffold.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单权限Mapper接口
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    
    /**
     * 根据用户ID查询菜单权限
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenusByUserId(@Param("userId") Long userId);
    
    /**
     * 根据角色ID查询菜单权限
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<Menu> selectMenusByRoleId(@Param("roleId") Integer roleId);
} 