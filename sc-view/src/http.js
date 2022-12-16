import axios from "axios";
const service = axios.create({
  baseURL: "http://localhost:8083/api/",
  timeout: 5000,
});

Axios.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";
Axios.defaults.withCredentials = true;

// http request 拦截器
Axios.interceptors.request.use((config) => {
  config.headers = {
    "Content-Type": "application/json", // 设置很关键
  };
  return config;
});
//为axios配置响应拦截器
service.interceptors.response.use(
  (res) => {
    return res;
  },
  (err) => {
    return Promise.reject(err);
  }
);

export default service;
