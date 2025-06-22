<template>
  <div class="user-container">
    <div class="header">
      <h2>用户管理</h2>
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">添加用户</el-button>
      </div>
    </div>
    
    <div class="search-bar">
      <el-form :model="queryParams" inline>
        <el-form-item label="用户名">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="真实姓名">
          <el-input v-model="queryParams.realName" placeholder="请输入真实姓名" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="部门">
          <el-input v-model="queryParams.department" placeholder="请输入部门" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <el-table
      v-loading="loading"
      :data="userList"
      border
      style="width: 100%"
    >
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="realName" label="真实姓名" />
      <el-table-column prop="department" label="部门" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column label="状态" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="primary" @click="handleRoles(scope.row)">分配角色</el-button>
          <el-button link type="primary" @click="handleResetPwd(scope.row)">重置密码</el-button>
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
    
    <!-- 用户表单对话框 -->
    <el-dialog
      :title="dialog.title"
      v-model="dialog.visible"
      width="500px"
      append-to-body
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userRules"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" :disabled="dialog.type === 'edit'" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="dialog.type === 'add'">
          <el-input v-model="userForm.password" placeholder="请输入密码" type="password" show-password />
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-input v-model="userForm.department" placeholder="请输入部门" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="userForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelForm">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </template>
    </el-dialog>
    
    <!-- 角色分配对话框 -->
    <el-dialog
      title="分配角色"
      v-model="roleDialog.visible"
      width="500px"
      append-to-body
    >
      <el-form label-width="80px">
        <el-form-item label="用户名">
          <div>{{ roleDialog.user.username }}</div>
        </el-form-item>
        <el-form-item label="角色">
          <el-checkbox-group v-model="roleDialog.selectedRoles">
            <el-checkbox
              v-for="role in roleList"
              :key="role.id"
              :label="role.id"
            >
              {{ role.roleName }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="roleDialog.visible = false">取 消</el-button>
          <el-button type="primary" @click="handleSaveRoles">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox, FormInstance } from 'element-plus';
import { getUserList, getUserDetail, updateUser, deleteUser, assignUserRoles, resetPassword, register } from '@/api/user';
import { getAllRoles } from '@/api/role';

// 加载状态
const loading = ref(false);

// 用户列表
const userList = ref<any[]>([]);

// 角色列表
const roleList = ref<any[]>([]);

// 总记录数
const total = ref(0);

// 查询参数
const queryParams = reactive({
  current: 1,
  size: 10,
  username: '',
  realName: '',
  department: ''
});

// 用户表单对话框
const dialog = reactive({
  visible: false,
  title: '',
  type: 'add' // add 或 edit
});

// 角色分配对话框
const roleDialog = reactive({
  visible: false,
  user: { id: 0, username: '' },
  selectedRoles: [] as number[]
});

// 用户表单
const userForm = reactive({
  id: undefined,
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  department: '',
  status: 1,
  remark: ''
});

// 表单校验规则
const userRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  department: [
    { required: true, message: '请输入部门', trigger: 'blur' }
  ]
};

const userFormRef = ref<FormInstance>();

// 查询用户列表
const getList = async () => {
  loading.value = true;
  try {
    const res = await getUserList(queryParams);
    userList.value = res.records || [];
    total.value = res.total || 0;
  } catch (err) {
    console.error('获取用户列表失败:', err);
  } finally {
    loading.value = false;
  }
};

// 查询角色列表
const getRoles = async () => {
  try {
    roleList.value = await getAllRoles();
  } catch (err) {
    console.error('获取角色列表失败:', err);
  }
};

// 查询按钮
const handleQuery = () => {
  queryParams.current = 1;
  getList();
};

// 重置查询
const resetQuery = () => {
  queryParams.username = '';
  queryParams.realName = '';
  queryParams.department = '';
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

// 添加用户
const handleAdd = () => {
  dialog.title = '添加用户';
  dialog.type = 'add';
  
  // 重置表单
  Object.assign(userForm, {
    id: undefined,
    username: '',
    password: '',
    realName: '',
    phone: '',
    email: '',
    department: '',
    status: 1,
    remark: ''
  });
  
  dialog.visible = true;
};

// 编辑用户
const handleEdit = async (row: any) => {
  dialog.title = '编辑用户';
  dialog.type = 'edit';
  
  try {
    const user = await getUserDetail(row.id);
    Object.assign(userForm, user);
    dialog.visible = true;
  } catch (err) {
    console.error('获取用户详情失败:', err);
    ElMessage.error('获取用户详情失败');
  }
};

// 提交表单
const submitForm = async () => {
  userFormRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        if (dialog.type === 'add') {
          // 添加用户逻辑
          await register(userForm);
          ElMessage.success('添加成功');
        } else {
          // 编辑用户逻辑
          await updateUser(userForm.id, userForm);
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

// 删除用户
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除用户 ${row.username} 吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUser(row.id);
      ElMessage.success('删除成功');
      getList();
    } catch (err) {
      console.error('删除失败:', err);
      ElMessage.error('删除失败');
    }
  }).catch(() => {});
};

// 重置密码
const handleResetPwd = (row: any) => {
  ElMessageBox.confirm(`确定要重置用户 ${row.username} 的密码吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await resetPassword(row.id);
      ElMessageBox.alert(`新密码: ${res}`, '密码重置成功', {
        confirmButtonText: '确定'
      });
    } catch (err) {
      console.error('重置密码失败:', err);
      ElMessage.error('重置密码失败');
    }
  }).catch(() => {});
};

// 打开分配角色对话框
const handleRoles = async (row: any) => {
  roleDialog.user = { id: row.id, username: row.username };
  roleDialog.visible = true;
  
  try {
    // 获取用户拥有的角色
    const user = await getUserDetail(row.id);
    if (user.roles && user.roles.length > 0) {
      roleDialog.selectedRoles = user.roles.map((role: any) => role.id);
    } else {
      roleDialog.selectedRoles = [];
    }
  } catch (err) {
    console.error('获取用户角色失败:', err);
    ElMessage.error('获取用户角色失败');
  }
};

// 保存用户角色
const handleSaveRoles = async () => {
  try {
    await assignUserRoles(roleDialog.user.id, roleDialog.selectedRoles);
    ElMessage.success('角色分配成功');
    roleDialog.visible = false;
  } catch (err) {
    console.error('角色分配失败:', err);
    ElMessage.error('角色分配失败');
  }
};

onMounted(() => {
  getList();
  getRoles();
});
</script>

<style scoped>
.user-container {
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
</style> 