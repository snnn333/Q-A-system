<template>
  <div class="top">
    <ul>
      <li class="link01">Q/A SYSTEM</li>
      <li><a href="#" id="link03"><i class="iconfont">&#xe625;</i>&nbsp&nbsp主页</a></li>
      <li class="link02"><a href="#"><i class="iconfont">&#xe7bf;</i>&nbsp&nbsp提问</a></li>
      <li class="link02">
        <a href="#"><i class="iconfont">&#xe627;</i>&nbsp&nbsp社区</a>
        <!-- <ul>
            <li><a href="#">二级菜单</a></li>
            <li><a href="#">二级菜单</a></li>
        </ul> -->
      </li>

      <div class="search bar">
        <form>
          <input type="text" placeholder="请输入您要搜索的内容...">
          <button type="submit"></button>
        </form>
      </div>
      <div class="buBox">
        <!-- 触发按钮 -->
        <button id="triggerBtn"><li><a href="#"><i class="iconfont">&#xe601;</i></a></li></button>

        </div>
    </ul>
  </div>
</template>

<script>
    export default {
        name: "home"
    }

</script>

<style scoped>
  @import "http://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css";
  @import "../components/css/buttonBox.css";
  @import "../assets/icon/iconfont.css";
  @import "css/searchBar2.css";
  @import "../components/css/top.css";
</style>


