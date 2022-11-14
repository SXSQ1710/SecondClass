export const getNowTime = () => {
    let date = new Date();
    
    let timeStr = date.getFullYear() + '-';
    timeStr += date.getMonth() + 1 + '-';
    timeStr += date.getDate()+ ' ';
    timeStr += date.getHours()+ ':' ;

    timeStr += date.getMinutes() < 10 ? '0' + new Date().getMinutes() : new Date().getMinutes()+ ':' ;
    timeStr += date.getSeconds() < 10 ? '0' + new Date().getSeconds() : new Date().getSeconds();
    return timeStr;
}