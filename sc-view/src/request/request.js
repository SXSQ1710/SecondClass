import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 提示错误
const errorLog = (mes) => {
    ElMessage({
        type: 'error',
        message: mes
    })
    // showClose: true,
    // duration: 5 * 1000
  }

// 创建一个 axios 实例, 配置默认值
const service = axios.create({
  headers: { 'Content-Type': 'application/json;charset=utf-8' },
  timeout: 120000 // 指定请求超时的毫秒数(0 表示无超时时间)
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 在请求发送之前做一些处理
    const token = localStorage.token
    // 判断是否存在token，如果存在的话，则每个http请求 header都加上token
    if (token) {
      config.headers.token = token
    }
    return config
  },
  error => {
    // 请求失败
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // dataAxios 是 axios 返回数据中的 data
    const dataAxios = response.data
    // 这个状态码是和后端约定的
    const code = dataAxios.code
    // 根据 code 进行判断
    if (code === undefined) {
      return dataAxios
    } else {
      switch (code) {
        case 0:
          // 正确的 code
          // 返回数据，axios().then(res) 中获取回调数据
          return dataAxios.data
        case 8888:
          // token失效, 跳转到登录页
          router.push('/user')
          errorLog('登陆失效，请重新登陆')
          break
        default:
          // 错误的 code
          errorLog(dataAxios.message)
          // 抛出错误，在 axios.().catch(err) 中可以获取回调数据
          return Promise.reject(dataAxios) 
      }
    }
  },
  error => {
    // 响应错误
    if (error && error.response) {
      switch (error.response.status) {
        case 400: error.message = '请求错误'; break
        case 401: error.message = '未授权，请登录'; break
        case 403: error.message = '拒绝访问'; break
        case 404: error.message = `请求地址出错: ${error.response.config.url}`; break
        case 408: error.message = '请求超时'; break
        case 500: error.message = '服务器内部错误'; break
        case 501: error.message = '服务未实现'; break
        case 502: error.message = '网关错误'; break
        case 503: error.message = '服务不可用'; break
        case 504: error.message = '网关超时'; break
        case 505: error.message = 'HTTP版本不受支持'; break
        default: break
      }
    }
    errorLog(error.message)
    return Promise.reject(error)
  }
)

export default service
