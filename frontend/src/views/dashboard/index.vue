<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="(card, index) in cardList" :key="index">
        <el-card shadow="hover" class="dashboard-card">
          <div class="card-content">
            <div class="card-icon" :style="{ backgroundColor: card.color }">
              <el-icon size="20"><component :is="card.icon" /></el-icon>
            </div>
            <div class="card-info">
              <div class="card-value">{{ card.value }}</div>
              <div class="card-title">{{ card.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="mt-4">
      <el-col :span="16">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>访问统计</span>
              <el-radio-group v-model="chartTimeRange" size="small">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">全年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div ref="visitChartRef" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>访问来源</span>
            </div>
          </template>
          <div ref="sourceChartRef" class="chart pie-chart"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card class="mt-4">
      <template #header>
        <div class="card-header">
          <span>系统信息</span>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="系统名称">{{ settings.title }}</el-descriptions-item>
        <el-descriptions-item label="系统版本">v1.0.0</el-descriptions-item>
        <el-descriptions-item label="前端框架">Vue 3.x + TypeScript + Element Plus</el-descriptions-item>
        <el-descriptions-item label="后端框架">Spring Boot 2.7.x + MyBatis-Plus</el-descriptions-item>
        <el-descriptions-item label="认证框架">Sa-Token</el-descriptions-item>
        <el-descriptions-item label="接口文档">Knife4j</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import settings from '@/config/settings'

// 统计卡片数据
const cardList = reactive([
  { title: '访问数', value: '2,325', icon: 'View', color: settings.theme.primary },
  { title: '用户数', value: '956', icon: 'User', color: settings.theme.success },
  { title: '订单数', value: '3,641', icon: 'ShoppingCart', color: settings.theme.warning },
  { title: '收入', value: '￥28,421', icon: 'Money', color: settings.theme.danger }
])

// 图表时间范围
const chartTimeRange = ref('month')

// 图表DOM引用
const visitChartRef = ref<HTMLElement | null>(null)
const sourceChartRef = ref<HTMLElement | null>(null)

// 图表实例
let visitChart: echarts.ECharts | null = null
let sourceChart: echarts.ECharts | null = null

// 初始化图表
const initCharts = () => {
  // 访问统计图
  if (visitChartRef.value) {
    visitChart = echarts.init(visitChartRef.value)
    const visitOption = {
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['访问量', '用户量']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '访问量',
          type: 'line',
          smooth: true,
          data: [120, 132, 101, 134, 90, 230, 210]
        },
        {
          name: '用户量',
          type: 'line',
          smooth: true,
          data: [45, 82, 35, 93, 42, 125, 62]
        }
      ]
    }
    visitChart.setOption(visitOption)
  }
  
  // 来源统计图
  if (sourceChartRef.value) {
    sourceChart = echarts.init(sourceChartRef.value)
    const sourceOption = {
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b}: {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: ['直接访问', '搜索引擎', '社交媒体', '外部链接', '其他']
      },
      series: [
        {
          name: '访问来源',
          type: 'pie',
          radius: '70%',
          center: ['50%', '50%'],
          data: [
            { value: 335, name: '直接访问' },
            { value: 310, name: '搜索引擎' },
            { value: 234, name: '社交媒体' },
            { value: 135, name: '外部链接' },
            { value: 148, name: '其他' }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    }
    sourceChart.setOption(sourceOption)
  }
}

// 窗口大小变化时重设图表大小
const resizeHandler = () => {
  visitChart?.resize()
  sourceChart?.resize()
}

onMounted(() => {
  initCharts()
  window.addEventListener('resize', resizeHandler)
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeHandler)
  visitChart?.dispose()
  sourceChart?.dispose()
})
</script>

<style scoped>
.dashboard-card {
  height: 120px;
}

.card-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.card-icon {
  @apply flex items-center justify-center rounded-full w-16 h-16 text-white;
}

.card-info {
  margin-left: 20px;
}

.card-value {
  @apply text-2xl font-bold text-gray-800;
}

.card-title {
  @apply text-sm text-gray-500 mt-1;
}

.card-header {
  @apply flex justify-between items-center;
}

.chart {
  height: 300px;
}

.pie-chart {
  height: 340px;
}

.mt-4 {
  margin-top: 16px;
}
</style> 