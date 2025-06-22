import request from '@/utils/request';

/**
 * 获取菜单列表
 * @param params 查询参数
 */
export function getMenuList(params: any) {
  return request({
    url: '/system/menu/list',
    method: 'get',
    params
  }).then(res => res.data || []);
}

/**
 * 获取菜单树
 */
export function getMenuTree() {
  return request({
    url: '/system/menu/treeList',
    method: 'get'
  }).then(res => res.data || []);
}

/**
 * 获取菜单详情
 * @param id 菜单ID
 */
export function getMenuDetail(id: number) {
  return request({
    url: `/menus/${id}`,
    method: 'get'
  });
}

/**
 * 添加菜单
 * @param data 菜单数据
 */
export function addMenu(data: any) {
  return request({
    url: '/system/menu',
    method: 'post',
    data
  });
}

/**
 * 更新菜单
 * @param id 菜单ID
 * @param data 菜单数据
 */
export function updateMenu(data: any) {
  return request({
    url: '/system/menu',
    method: 'put',
    data
  });
}

/**
 * 删除菜单
 * @param id 菜单ID
 */
export function deleteMenu(menuId: number) {
  return request({
    url: `/system/menu/${menuId}`,
    method: 'delete'
  });
}

/**
 * 获取角色菜单ID列表
 * @param roleId 角色ID
 */
export function getRoleMenuIds(roleId: number) {
  return request({
    url: `/system/menu/roleMenuIds/${roleId}`,
    method: 'get'
  }).then(res => res.data || []);
}

/**
 * 分配角色菜单
 * @param roleId 角色ID
 * @param menuIds 菜单ID列表
 */
export function assignRoleMenus(roleId: number, menuIds: number[]) {
  return request({
    url: `/system/menu/assignRoleMenus`,
    method: 'post',
    data: {
      roleId,
      menuIds
    }
  });
}

/**
 * 获取用户菜单
 */
export function getUserMenus() {
  return request({
    url: '/system/menu/getUserMenus',
    method: 'get'
  }).then(res => res.data || []);
}

/**
 * 获取用户权限标识
 */
export function getUserPermissions() {
  return request({
    url: '/menus/user/perms',
    method: 'get'
  });
} 