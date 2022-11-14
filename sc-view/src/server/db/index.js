let mysql=require('mysql')

let db=mysql.createPool({
    host:'localhost',            //数据库IP地址
    port:'3306',
    user:'root',                //数据库登录账号
    password:'ao171511',     //数据库登录密码
    database:'secondclass',           //要操作的数据库
    dateStrings:true, //2022-09-15T22:43:11.000Z  删除TZ
})

module.exports=db;
