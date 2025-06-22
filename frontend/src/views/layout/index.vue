<template>
  <div class="layout">
    <el-container class="layout-container">
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
        <div class="logo" :class="{ 'collapsed': isCollapse }">
          <img src="@/assets/logo.png" alt="logo" class="logo-img" v-if="false" />
          <h1 v-if="!isCollapse">{{ settings.title }}</h1>
        </div>
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          unique-opened
          router
          class="menu"
        >
          <el-menu-item index="/dashboard">
            <el-icon><Odometer /></el-icon>
            <template #title>仪表盘</template>
          </el-menu-item>
          <el-sub-menu index="system">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/system/user">用户管理</el-menu-item>
            <el-menu-item index="/system/role">角色管理</el-menu-item>
            <el-menu-item index="/system/menu">菜单管理</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      
      <!-- 主体区域 -->
      <el-container class="main-container">
        <!-- 头部 -->
        <el-header class="header">
          <div class="left">
            <el-icon class="fold-icon" @click="toggleSidebar">
              <component :is="isCollapse ? 'Expand' : 'Fold'" />
            </el-icon>
            <breadcrumb />
          </div>
          <div class="right">
            <el-dropdown trigger="click">
              <div class="avatar-container">
                <el-avatar size="small" icon="UserFilled" />
                <span class="username">管理员</span>
                <el-icon><CaretBottom /></el-icon>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleProfile">个人信息</el-dropdown-item>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- 内容区 -->
        <el-main class="main">
          <router-view v-slot="{ Component }">
            <component :is="Component" />
          </router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import settings from '@/config/settings'
import { logout } from '@/api/user'

// 组件
const Breadcrumb = defineComponent({
  name: 'Breadcrumb',
  setup() {
    return () => h('div', { class: 'breadcrumb' }, 'Dashboard')
  }
})

const router = useRouter()
const route = useRoute()

// 侧边栏折叠状态
const isCollapse = ref(false)

// 当前激活菜单
const activeMenu = computed(() => {
  return route.path
})

// 切换侧边栏
const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

// 查看个人信息
const handleProfile = () => {
  router.push('/profile')
}

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await logout()
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      router.push('/login')
    } catch (error) {
      console.error('退出登录失败:', error)
    }
  }).catch(() => {})
}
</script>

<style scoped>
.layout {
  @apply h-screen;
}

.layout-container {
  @apply h-full;
}

.aside {
  @apply bg-gray-800 text-white transition-all duration-300 flex flex-col;
  overflow: hidden;
}

.logo {
  @apply h-16 flex items-center justify-center border-b border-gray-700;
}

.logo h1 {
  @apply text-lg font-bold m-0 text-white;
}

.logo.collapsed {
  @apply justify-center;
}

.menu {
  @apply border-0 flex-1;
  --el-menu-bg-color: transparent;
  --el-menu-text-color: #fff;
  --el-menu-hover-bg-color: #1f2937;
  --el-menu-active-color: #409eff;
}

.header {
  @apply bg-white border-b border-gray-200 flex items-center justify-between px-4;
}

.header .left {
  @apply flex items-center;
}

.fold-icon {
  @apply text-xl cursor-pointer mr-4;
}

.avatar-container {
  @apply flex items-center cursor-pointer;
}

.username {
  @apply mx-2;
}

.main {
  @apply bg-gray-100 p-4;
}
</style> 