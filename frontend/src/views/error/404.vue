<template>
  <div class="error-page">
    <div class="error-content">
      <el-result
        icon="error"
        title="404"
        sub-title="抱歉，您访问的页面不存在"
      >
        <template #extra>
          <el-button type="primary" @click="goBack">返回</el-button>
          <el-button @click="goHome">首页</el-button>
        </template>
      </el-result>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.go(-1)
}

const goHome = () => {
  router.push('/')
}
</script>

<style scoped>
.error-page {
  @apply flex items-center justify-center min-h-screen;
}
.error-content {
  @apply text-center;
}
</style> 