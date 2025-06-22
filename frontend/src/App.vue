<script setup lang="ts">
import settings from './config/settings'
import { onMounted } from 'vue'

onMounted(() => {
  document.title = settings.title
})
</script>

<template>
  <router-view />
</template>

<style>
@import './index.css';
</style>
