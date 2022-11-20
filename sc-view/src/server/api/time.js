// 获取系统时间
export const getNowTime = () => {
    let date = new Date();
    let timeStr = date.getFullYear() + '-';
    timeStr += date.getMonth() + 1 + '-';
    timeStr += date.getDate()+ ' ';
    timeStr += date.getHours()+ ':' ;

    timeStr += date.getMinutes() < 10 ? '0' + new Date().getMinutes() : new Date().getMinutes()
    timeStr +=":"
    timeStr += date.getSeconds() < 10 ? '0' + new Date().getSeconds() : new Date().getSeconds();
    return timeStr;
}
// 时间戳转换为日期时间
export const timestampToTime = (timestamp) => {
    timestamp = timestamp ? timestamp : null;
    let date = new Date(timestamp);//时间戳为10位也就是秒需*1000，时间戳为13位也就是毫秒的话不需乘1000
    let Y = date.getFullYear() + '-';
    let M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    let D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
    let h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    let m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    let s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
    return Y + M + D + h + m + s;
 }
//  格林威治时间转化为日期时间格式
export const GMTToStr=(time)=>{
    let date = new Date(time)
    let Str=date.getFullYear() + '-' +
    (date.getMonth() + 1) + '-' + 
    date.getDate() + ' ' + 
    date.getHours() + ':' + 
    date.getMinutes() + ':' + 
    date.getSeconds()
    return Str
}