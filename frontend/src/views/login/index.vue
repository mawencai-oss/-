<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">
        <h2>{{ settings.title }}</h2>
        <p>{{ settings.subTitle }}</p>
      </div>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
            size="large"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" type="primary" class="w-full" size="large" @click="handleLogin">
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="login-footer">
      <p>{{ settings.copyright }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, FormInstance } from 'element-plus'
import { login } from '@/api/user'
import settings from '@/config/settings'

const router = useRouter()
const route = useRoute()

const loginForm = reactive({
  username: 'admin',
  password: '123456'
})

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const loading = ref(false)
const loginFormRef = ref<FormInstance>()

const handleLogin = () => {
  loginFormRef.value?.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      const res = await login(loginForm)
      const { token, user } = res.data
      
      localStorage.setItem('token', token)
      localStorage.setItem('userInfo', JSON.stringify(user))
      
      ElMessage.success('登录成功')
      
      const redirect = route.query.redirect as string || '/'
      router.push(redirect)
    } catch (error) {
      console.error('登录失败:', error)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-container {
  @apply flex flex-col justify-between min-h-screen bg-gradient-to-r from-blue-500 to-indigo-600;
}

.login-box {
  @apply bg-white rounded-lg shadow-xl p-8 w-96 mx-auto my-20;
}

.login-title {
  @apply text-center mb-8;
}

.login-title h2 {
  @apply text-2xl font-bold text-gray-800;
}

.login-title p {
  @apply text-gray-500 mt-1;
}

.login-form {
  @apply w-full;
}

.login-footer {
  @apply text-center text-white py-4;
}
</style> 