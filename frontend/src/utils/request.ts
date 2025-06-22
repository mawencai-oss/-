import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 请求成功
    if (res.code === 200) {
      return res
    }
    
    // 处理特殊状态码
    if (res.code === 401) {
      ElMessage.error('登录已过期，请重新登录')
      localStorage.removeItem('token')
      router.push('/login')
    } else {
      ElMessage.error(res.msg || '服务器响应错误')
    }
    
    return Promise.reject(res)
  },
  error => {
    console.error('响应错误:', error)
    const message = error.response?.data?.msg || '请求失败，请稍后重试'
    ElMessage.error(message)
    
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      router.push('/login')
    }
    
    return Promise.reject(error)
  }
)

export default request 