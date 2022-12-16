import axios from "axios";
const service = axios.create({
  baseURL: "http://127.0.0.1:8083/",
  timeout: 5000
});

// 配置axios的请求拦截器
service.interceptors.request.use(config=>{
    return config;
})


// 配置axios的响应拦截器
service.interceptors.response.use(res=>{
    return res;
});

export default service;
