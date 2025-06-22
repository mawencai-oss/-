import request from '@/utils/request';

/**
 * 获取角色列表（分页）
 * @param params 查询参数
 */
export function getRoleList(params: any) {
  return request({
    url: '/roles/list',
    method: 'get',
    params
  });
}

/**
 * 获取所有角色（不分页）
 */
export function getAllRoles() {
  return request({
    url: '/roles/all',
    method: 'get'
  });
}

/**
 * 获取角色详情
 * @param id 角色ID
 */
export function getRoleDetail(id: number) {
  return request({
    url: `/roles/${id}`,
    method: 'get'
  });
}

/**
 * 添加角色
 * @param data 角色信息
 */
export function addRole(data: any) {
  return request({
    url: '/roles',
    method: 'post',
    data
  });
}

/**
 * 更新角色
 * @param id 角色ID
 * @param data 角色信息
 */
export function updateRole(id: number, data: any) {
  return request({
    url: `/roles/${id}`,
    method: 'put',
    data
  });
}

/**
 * 删除角色
 * @param id 角色ID
 */
export function deleteRole(id: number) {
  return request({
    url: `/roles/${id}`,
    method: 'delete'
  });
} 