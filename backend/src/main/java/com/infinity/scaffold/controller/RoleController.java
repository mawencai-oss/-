package com.infinity.scaffold.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.infinity.scaffold.common.Result;
import com.infinity.scaffold.entity.Role;
import com.infinity.scaffold.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    /**
     * 获取角色列表
     */
    @ApiOperation("获取角色列表")
    @SaCheckLogin
    @GetMapping("/list")
    public Result<Page<Role>> list(@RequestParam(defaultValue = "1") Integer current,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) String roleName,
                                   @RequestParam(required = false) String roleCode) {
        Page<Role> page = new Page<>(current, size);
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(roleName)) {
            queryWrapper.like(Role::getRoleName, roleName);
        }
        if (StringUtils.hasText(roleCode)) {
            queryWrapper.like(Role::getRoleCode, roleCode);
        }
        
        // 排序
        queryWrapper.orderByAsc(Role::getSort);
        
        Page<Role> rolePage = roleService.page(page, queryWrapper);
        return Result.success(rolePage);
    }
    
    /**
     * 获取所有角色（不分页）
     */
    @ApiOperation("获取所有角色")
    @SaCheckLogin
    @GetMapping("/all")
    public Result<List<Role>> listAll() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Role::getSort);
        List<Role> roleList = roleService.list(queryWrapper);
        return Result.success(roleList);
    }

    /**
     * 获取角色详情
     */
    @ApiOperation("获取角色详情")
    @SaCheckLogin
    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable Integer id) {
        Role role = roleService.getById(id);
        return Result.success(role);
    }

    /**
     * 添加角色
     */
    @ApiOperation("添加角色")
    @SaCheckRole("admin")
    @PostMapping
    public Result<Role> add(@RequestBody Role role) {
        // 检查角色编码是否存在
        Role existRole = roleService.getByRoleCode(role.getRoleCode());
        if (existRole != null) {
            return Result.error("角色编码已存在");
        }
        
        if (roleService.save(role)) {
            return Result.success("添加成功", role);
        } else {
            return Result.error("添加失败");
        }
    }

    /**
     * 更新角色
     */
    @ApiOperation("更新角色")
    @SaCheckRole("admin")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, @RequestBody Role role) {
        role.setId(id);
        
        // 检查角色编码是否重复
        Role existRole = roleService.getByRoleCode(role.getRoleCode());
        if (existRole != null && !existRole.getId().equals(id)) {
            return Result.error("角色编码已存在");
        }
        
        if (roleService.updateById(role)) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        if (roleService.removeById(id)) {
            return Result.success();
        } else {
            return Result.error("删除失败");
        }
    }
} 