// 配置对应路由
let express=require('express')
let router=express.Router()
let select_all=require('./api/select_all')


router.get('/select_user',select_all.get_user)
router.get('/select_org',select_all.get_org)
router.get('/select_activity',select_all.get_activity)

module.exports=router
