<template>
  <div class="role-container">
    <div class="header">
      <h2>角色管理</h2>
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">添加角色</el-button>
      </div>
    </div>
    
    <div class="search-bar">
      <el-form :model="queryParams" inline>
        <el-form-item label="角色名称">
          <el-input v-model="queryParams.roleName" placeholder="请输入角色名称" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="queryParams.roleCode" placeholder="请输入角色编码" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <el-table
      v-loading="loading"
      :data="roleList"
      border
      style="width: 100%"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleCode" label="角色编码" />
      <el-table-column prop="description" label="描述" />
      <el-table-column label="状态" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="240" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="primary" @click="handleMenuAssign(scope.row)">分配权限</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="queryParams.current"
        v-model:page-size="queryParams.size"
        :page-sizes="[10, 20, 30, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    
    <!-- 角色表单对话框 -->
    <el-dialog
      :title="dialog.title"
      v-model="dialog.visible"
      width="500px"
      append-to-body
    >
      <el-form
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleRules"
        label-width="80px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" :disabled="dialog.type === 'edit'" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="roleForm.sort" :min="0" :max="999" controls-position="right" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="roleForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="roleForm.description" type="textarea" placeholder="请输入角色描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelForm">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 菜单权限分配对话框 -->
    <el-dialog
      title="分配菜单权限"
      v-model="menuDialog.visible"
      width="500px"
      append-to-body
    >
      <div v-if="menuDialog.role.id">
        <div class="dialog-role-info">
          <p><strong>角色名称：</strong>{{ menuDialog.role.roleName }}</p>
          <p><strong>角色编码：</strong>{{ menuDialog.role.roleCode }}</p>
        </div>
        <div class="dialog-menu-tree">
          <el-tree
            ref="menuTreeRef"
            node-key="id"
            :data="menuTree"
            :props="{ label: 'menuName', children: 'children' }"
            show-checkbox
            default-expand-all
            :default-checked-keys="menuDialog.checkedKeys"
          />
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="menuDialog.visible = false">取 消</el-button>
          <el-button type="primary" @click="handleSaveRoleMenus">保 存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus';
import { getRoleList, getRoleDetail, addRole, updateRole, deleteRole } from '@/api/role';
import { getMenuTree, getRoleMenuIds, assignRoleMenus } from '@/api/menu';
import type { ElTree } from 'element-plus';

// 加载状态
const loading = ref(false);

// 角色列表
const roleList = ref<any[]>([]);

// 菜单树数据
const menuTree = ref<any[]>([]);

// 总记录数
const total = ref(0);

// 查询参数
const queryParams = reactive({
  current: 1,
  size: 10,
  roleName: '',
  roleCode: ''
});

// 角色表单对话框
const dialog = reactive({
  visible: false,
  title: '',
  type: 'add' // add 或 edit
});

// 菜单权限对话框
const menuDialog = reactive({
  visible: false,
  role: { id: 0, roleName: '', roleCode: '' },
  checkedKeys: [] as number[]
});

// 角色表单
const roleForm = reactive({
  id: undefined,
  roleName: '',
  roleCode: '',
  sort: 0,
  status: 1,
  description: ''
});

// 表单校验规则
const roleRules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' }
  ]
};

const roleFormRef = ref<FormInstance>();
const menuTreeRef = ref<InstanceType<typeof ElTree>>();

// 查询角色列表
const getList = async () => {
  loading.value = true;
  try {
    const res = await getRoleList(queryParams);
    roleList.value = res.records || [];
    total.value = res.total || 0;
  } catch (err) {
    console.error('获取角色列表失败:', err);
  } finally {
    loading.value = false;
  }
};

// 获取菜单树
const getMenuTreeData = async () => {
  try {
    menuTree.value = await getMenuTree();
  } catch (err) {
    console.error('获取菜单树失败:', err);
  }
};

// 查询按钮
const handleQuery = () => {
  queryParams.current = 1;
  getList();
};

// 重置查询
const resetQuery = () => {
  queryParams.roleName = '';
  queryParams.roleCode = '';
  handleQuery();
};

// 改变每页数量
const handleSizeChange = (val: number) => {
  queryParams.size = val;
  getList();
};

// 改变页码
const handleCurrentChange = (val: number) => {
  queryParams.current = val;
  getList();
};

// 添加角色
const handleAdd = () => {
  dialog.title = '添加角色';
  dialog.type = 'add';
  
  // 重置表单
  Object.assign(roleForm, {
    id: undefined,
    roleName: '',
    roleCode: '',
    sort: 0,
    status: 1,
    description: ''
  });
  
  dialog.visible = true;
};

// 编辑角色
const handleEdit = async (row: any) => {
  dialog.title = '编辑角色';
  dialog.type = 'edit';
  
  try {
    const role = await getRoleDetail(row.id);
    Object.assign(roleForm, role);
    dialog.visible = true;
  } catch (err) {
    console.error('获取角色详情失败:', err);
    ElMessage.error('获取角色详情失败');
  }
};

// 提交表单
const submitForm = async () => {
  roleFormRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        if (dialog.type === 'add') {
          // 添加角色
          await addRole(roleForm);
          ElMessage.success('添加成功');
        } else {
          // 编辑角色
          await updateRole(roleForm.id, roleForm);
          ElMessage.success('更新成功');
        }
        
        dialog.visible = false;
        getList();
      } catch (err) {
        console.error('操作失败:', err);
        ElMessage.error('操作失败');
      }
    }
  });
};

// 取消表单
const cancelForm = () => {
  dialog.visible = false;
};

// 删除角色
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除角色 ${row.roleName} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(row.id);
      ElMessage.success('删除成功');
      getList();
    } catch (err) {
      console.error('删除失败:', err);
      ElMessage.error('删除失败');
    }
  }).catch(() => {});
};

// 打开菜单权限分配对话框
const handleMenuAssign = async (row: any) => {
  menuDialog.role = { id: row.id, roleName: row.roleName, roleCode: row.roleCode };
  menuDialog.visible = true;
  
  try {
    // 获取角色已有的菜单权限
    menuDialog.checkedKeys = await getRoleMenuIds(row.id);
  } catch (err) {
    console.error('获取角色菜单失败:', err);
    ElMessage.error('获取角色菜单失败');
  }
};

// 保存角色菜单权限
const handleSaveRoleMenus = async () => {
  if (!menuDialog.role.id) return;
  
  const checkedKeys = menuTreeRef.value?.getCheckedKeys() as number[];
  const halfCheckedKeys = menuTreeRef.value?.getHalfCheckedKeys() as number[];
  
  // 合并选中和半选节点
  const menuIds = [...checkedKeys, ...halfCheckedKeys];
  
  try {
    await assignRoleMenus(menuDialog.role.id, menuIds);
    ElMessage.success('菜单权限分配成功');
    menuDialog.visible = false;
  } catch (err) {
    console.error('菜单权限分配失败:', err);
    ElMessage.error('菜单权限分配失败');
  }
};

onMounted(() => {
  getList();
  getMenuTreeData();
});
</script>

<style scoped>
.role-container {
  @apply bg-white p-5 rounded-md;
}

.header {
  @apply flex justify-between items-center mb-5;
}

.header h2 {
  @apply text-xl font-bold m-0;
}

.search-bar {
  @apply mb-5;
}

.pagination-container {
  @apply mt-5 flex justify-end;
}

.dialog-footer {
  @apply flex justify-end;
}

.dialog-role-info {
  @apply mb-4 border-b pb-2;
}

.dialog-menu-tree {
  @apply mt-4 max-h-80 overflow-auto;
}
</style> 