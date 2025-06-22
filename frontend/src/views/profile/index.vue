<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="info-card">
          <template #header>
            <div class="info-header">
              <span>个人信息</span>
            </div>
          </template>
          <div class="info-content">
            <div class="avatar-box">
              <el-avatar :size="100" icon="UserFilled" />
            </div>
            <ul class="info-list">
              <li>
                <label>用户名：</label>
                <span>{{ userInfo.username }}</span>
              </li>
              <li>
                <label>真实姓名：</label>
                <span>{{ userInfo.realName }}</span>
              </li>
              <li>
                <label>部门：</label>
                <span>{{ userInfo.department }}</span>
              </li>
              <li>
                <label>手机号：</label>
                <span>{{ userInfo.phone }}</span>
              </li>
              <li>
                <label>邮箱：</label>
                <span>{{ userInfo.email }}</span>
              </li>
              <li>
                <label>角色：</label>
                <span>
                  <el-tag v-for="role in roles" :key="role.id" style="margin-right: 5px">
                    {{ role.roleName }}
                  </el-tag>
                </span>
              </li>
              <li>
                <label>创建时间：</label>
                <span>{{ userInfo.createTime }}</span>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <el-tabs v-model="activeTab">
                <el-tab-pane label="基本信息" name="basic">
                  <template #label>
                    <el-icon><User /></el-icon>基本资料
                  </template>
                </el-tab-pane>
                <el-tab-pane label="修改密码" name="password">
                  <template #label>
                    <el-icon><Lock /></el-icon>修改密码
                  </template>
                </el-tab-pane>
              </el-tabs>
            </div>
          </template>

          <!-- 基本资料 -->
          <el-form
            v-if="activeTab === 'basic'"
            ref="userFormRef"
            :model="userForm"
            :rules="userRules"
            label-width="100px"
          >
            <el-form-item label="用户名" prop="username">
              <el-input v-model="userForm.username" disabled />
            </el-form-item>
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
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
            <el-form-item>
              <el-button type="primary" @click="updateProfile">保存</el-button>
            </el-form-item>
          </el-form>

          <!-- 修改密码 -->
          <el-form
            v-if="activeTab === 'password'"
            ref="pwdFormRef"
            :model="pwdForm"
            :rules="pwdRules"
            label-width="100px"
          >
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input v-model="pwdForm.oldPassword" type="password" show-password placeholder="请输入旧密码" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="请输入新密码" />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="pwdForm.confirmPassword" type="password" show-password placeholder="请再次输入新密码" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="updatePassword">保存</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, FormInstance } from 'element-plus';
import { getUserInfo, updateUser, changePassword } from '@/api/user';

// 当前激活的标签页
const activeTab = ref('basic');

// 用户信息
const userInfo = reactive({
  id: '',
  username: '',
  realName: '',
  department: '',
  phone: '',
  email: '',
  createTime: ''
});

// 角色列表
const roles = ref<any[]>([]);

// 用户表单
const userForm = reactive({
  id: '',
  username: '',
  realName: '',
  department: '',
  phone: '',
  email: ''
});

// 密码表单
const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

// 表单校验规则
const userRules = {
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  department: [{ required: true, message: '请输入部门', trigger: 'blur' }]
};

// 密码校验规则
const pwdRules = {
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string, callback: any) => {
        if (value !== pwdForm.newPassword) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
};

const userFormRef = ref<FormInstance>();
const pwdFormRef = ref<FormInstance>();

// 获取用户信息
const getUserProfile = async () => {
  try {
    const result = await getUserInfo();
    const { user, roles: userRoles } = result;
    
    // 更新用户信息
    Object.assign(userInfo, user);
    
    // 更新表单数据
    Object.assign(userForm, user);
    
    // 更新角色
    roles.value = userRoles || [];
  } catch (err) {
    console.error('获取用户信息失败:', err);
    ElMessage.error('获取用户信息失败');
  }
};

// 更新个人信息
const updateProfile = async () => {
  userFormRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        await updateUser(userForm.id, userForm);
        ElMessage.success('更新成功');
        getUserProfile();
      } catch (err) {
        console.error('更新失败:', err);
        ElMessage.error('更新失败');
      }
    }
  });
};

// 修改密码
const updatePassword = async () => {
  pwdFormRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        await changePassword(pwdForm.oldPassword, pwdForm.newPassword);
        ElMessage.success('密码修改成功');
        
        // 重置表单
        pwdForm.oldPassword = '';
        pwdForm.newPassword = '';
        pwdForm.confirmPassword = '';
      } catch (err) {
        console.error('密码修改失败:', err);
        ElMessage.error('密码修改失败');
      }
    }
  });
};

onMounted(() => {
  getUserProfile();
});
</script>

<style scoped>
.profile-container {
  padding: 20px;
}

.info-card {
  margin-bottom: 20px;
}

.info-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.info-content {
  padding: 10px 0;
}

.avatar-box {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.info-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.info-list li {
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}

.info-list li:last-child {
  border-bottom: none;
}

.info-list label {
  font-weight: bold;
  margin-right: 10px;
  display: inline-block;
  width: 80px;
}

.card-header {
  padding: 0;
}
</style> 