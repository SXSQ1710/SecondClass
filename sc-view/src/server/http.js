import axios from "axios";
const service = axios.create({
  baseURL: "http://127.0.0.1:8083/api/",
  timeout: 5000,
});

axios.defaults.withCredentials=true //让ajax携带cookie

//为axios配置请求拦截器
service.interceptors.request.use(config=>{
  //获取token值存储在请求头，令牌
  // config.headers['Cookie'] = getTokenName()+"="+getTokenValue()
  config.headers = {
    'Content-Type': 'application/json' //  注意：设置很关键 
  }
  return config;
})
//为axios配置响应拦截器
service.interceptors.response.use(res=>{
  return res;
},err => {
  return Promise.reject(err);
});



export default service;
