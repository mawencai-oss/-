package com.infinity.scaffold.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.infinity.scaffold.common.Result;
import com.infinity.scaffold.entity.Menu;
import com.infinity.scaffold.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单权限控制器
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final IMenuService menuService;

    /**
     * 获取菜单列表
     */
    @ApiOperation("获取菜单列表")
    @SaCheckRole("admin")
    @GetMapping("/list")
    public Result<List<Menu>> list(@RequestParam(required = false) String menuName,
                                  @RequestParam(required = false) String status) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        
        // 添加查询条件
        if (StringUtils.hasText(menuName)) {
            queryWrapper.like(Menu::getMenuName, menuName);
        }
        if (StringUtils.hasText(status)) {
            queryWrapper.eq(Menu::getStatus, status);
        }
        
        // 排序
        queryWrapper.orderByAsc(Menu::getParentId)
                .orderByAsc(Menu::getOrderNum);
        
        List<Menu> list = menuService.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * 获取菜单详情
     */
    @ApiOperation("获取菜单详情")
    @SaCheckRole("admin")
    @GetMapping("/{id}")
    public Result<Menu> getInfo(@PathVariable Integer id) {
        Menu menu = menuService.getById(id);
        return Result.success(menu);
    }

    /**
     * 添加菜单
     */
    @ApiOperation("添加菜单")
    @SaCheckRole("admin")
    @PostMapping
    public Result<Void> add(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.success();
    }

    /**
     * 修改菜单
     */
    @ApiOperation("修改菜单")
    @SaCheckRole("admin")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, @RequestBody Menu menu) {
        menu.setId(id);
        menuService.updateById(menu);
        return Result.success();
    }

    /**
     * 删除菜单
     */
    @ApiOperation("删除菜单")
    @SaCheckRole("admin")
    @DeleteMapping("/{id}")
    public Result<Void> remove(@PathVariable Integer id) {
        // 检查是否有子菜单
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, id);
        int count = menuService.count(queryWrapper);
        if (count > 0) {
            return Result.error("存在子菜单，不允许删除");
        }
        
        menuService.removeById(id);
        return Result.success();
    }

    /**
     * 获取菜单树
     */
    @ApiOperation("获取菜单树")
    @SaCheckRole("admin")
    @GetMapping("/tree")
    public Result<List<Menu>> tree() {
        List<Menu> allMenus = menuService.list(new LambdaQueryWrapper<Menu>()
                .orderByAsc(Menu::getParentId)
                .orderByAsc(Menu::getOrderNum));
        List<Menu> menuTree = menuService.buildMenuTree(allMenus);
        return Result.success(menuTree);
    }

    /**
     * 获取角色菜单列表
     */
    @ApiOperation("获取角色菜单列表")
    @SaCheckRole("admin")
    @GetMapping("/role/{roleId}")
    public Result<List<Integer>> getRoleMenus(@PathVariable Integer roleId) {
        List<Integer> menuIds = menuService.selectMenuListByRoleId(roleId);
        return Result.success(menuIds);
    }

    /**
     * 分配角色菜单
     */
    @ApiOperation("分配角色菜单")
    @SaCheckRole("admin")
    @PostMapping("/role/{roleId}")
    public Result<Void> assignMenus(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        menuService.assignMenusToRole(roleId, menuIds);
        return Result.success();
    }

    /**
     * 获取用户菜单列表
     */
    @ApiOperation("获取用户菜单列表")
    @SaCheckLogin
    @GetMapping("/user")
    public Result<List<Menu>> getUserMenus() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<Menu> menuTree = menuService.selectMenuTreeByUserId(userId);
        return Result.success(menuTree);
    }

    /**
     * 获取用户权限标识
     */
    @ApiOperation("获取用户权限标识")
    @SaCheckLogin
    @GetMapping("/user/perms")
    public Result<List<String>> getUserPerms() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<String> perms = new ArrayList<>(menuService.getUserPermissions(userId));
        return Result.success(perms);
    }
} 