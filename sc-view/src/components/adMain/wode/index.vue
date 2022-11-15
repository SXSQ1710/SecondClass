<template>
    <div class="welcome">
        <h1 class="helloword">学生工作系统管理后台</h1>
        <h2 class="helloword">欢迎您！</h2>
    </div>
</template>
    
<script>
export default {
    name: "wode"
}
</script>
<style>
.welcome{
    padding: 2vw;
    background-color: rgba(204,202,220,0.33);
}
menu.dark > .welcome{
    padding: 2vw;
    background-color: rgba(192, 190, 214, 0.841);
}
menu.dark > .helloword{
    margin: 30px auto;
    color: #efe4e4;
}

.helloword{
    margin: 30px auto;
    color: #4d4d4d;
}
</style>