<template>

  <div>
    <!--弹出框-->
    <el-dialog
            title="提示"
            :visible.sync="dialogVisible"
            width="30%"
            :before-close="handleClose">
      <span>即将登录，是否前往登录</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="danger" @click="toLogin">确 定</el-button>
  </span>
    </el-dialog>
    <!--导航栏-->
    <div class="top">
      <ul class="nav" style="padding-left: 9%;">
        <li class="link01">:D&nbsp&nbsp答</li>
        <li class="nav-item">
          <a class="nav-link" href="#" id="link03"><i class="iconfont">&#xe625;</i>&nbsp&nbsp主页</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" @click="toQuestion"><i class="iconfont">&#xe7bf;</i>&nbsp&nbsp问题</a>
        </li>
        <li class="nav-item">
          <a class="nav-link"><i class="iconfont">&#xe627;</i>&nbsp&nbsp社区</a>
        </li>
        <div class="search bar">
          <form>
            <input type="text" placeholder="请输入您要搜索的内容...">
            <button id="searchBtn" type="submit"></button>
          </form>
        </div>
        <div class="buBox">
          <!-- 触发按钮 -->
          <div v-if="hasNotLogin[0]">
          <button id="triggerBtn" @click="dialogVisible = true"><li><a href="#" data-toggle="tooltip" data-placement="bottom" title="登录"><i class="iconfont">&#xe601;</i></a></li></button>
          </div>
          <div v-else>
          <button id="personBtn">
            <div @click="toPersonalHome">
              <ul>
            <li style="float:left;margin-top: -4px">
              <a data-toggle="tooltip" data-placement="bottom" title="个人中心"><el-avatar :size="35" :src="circleUrl"></el-avatar></a>
            </li>
            <li style="float:left;">
              <a data-toggle="tooltip" data-placement="bottom" title="个人中心" style="text-align: end">{{myName}}</a>
            </li>
          </ul>
          </div>
          </button>
        </div>
        </div>
      </ul>
<!--      <hr color="#FFF" style="margin-bottom: 0;padding-bottom: 0">-->
    </div>

    <br> <br> <br> <br>
    <!--问题展示-->
    <div id="disQue">
      <div class="tabs">
        <ul class="nav nav-tabs">
          <li class="nav-item"><div class="tab" @click="toggleTab('hot')"><a :class="{active:currentTab=='hot' }">热帖</a></div></li>
          <li class="nav-item"><div class="tab" @click="toggleTab('tq')"><a :class="{active:currentTab=='tq'}">最新</a></div></li>
        </ul>
      </div>
      <hot :is="currentTab" keep-alive></hot>
    </div>
    <!--左侧边栏-->
    <div id="disRight">
      <!--卡片1-->
      <div class="cright">
          <h5 class="card-title">Q/A START</h5>
          <div class="btns" style="margin-top:0;">
          <button @click="toQuestion"><i class="iconfont">&#xe628;</i>写回答</button>
          <button @click="toQuestionSubmit"><i class="iconfont">&#xe703;</i>写问题</button>
          </div>
      </div>
      <!--卡片2-->
      <div class="cright" v-show="!hasNotLogin[0]">
            <button class="rightBtn"><a><i class="font2 iconfont">&#xe613;</i>&nbsp&nbsp我的收藏</a></button><br><br>
            <button class="rightBtn"><a><i class="font3 iconfont">&#xe60c;</i>&nbsp&nbsp我的打赏</a></button><br><br>
            <button class="rightBtn"><a><i class="font4 iconfont">&#xe60d;</i>&nbsp&nbsp我的关注</a></button>
      </div>
      <!--轮播-->
      <div class="cright">
      <div class="block">

        <el-carousel height="300px">
          <el-carousel-item>
            <img src="../assets/pic/a.jpg">
          </el-carousel-item>
          <el-carousel-item>
            <img src="../assets/pic/b.jpg">
          </el-carousel-item>
          <el-carousel-item>
            <img src="../assets/pic/c.jpg">
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
    </div>
  </div>

</template>

<script>
  /*import util from '../util.js'*/
  import hot from './hot.vue';
  import tq from './time.vue';
  import global from './global.vue'
  var hasNotLogin = [1]
    export default {
        name: "homeTest",
      data(){
          return{
            currentTab:'hot',
            circleUrl: global.photo,
            hasNotLogin:hasNotLogin,
            myId:global.userId,
            myName:global.name,
            dialogVisible:false
          }
    },
      components:{
        hot,
        tq
      },
      created(){
        console.log("create"+global.userId)
          if(global.userId===-1){
            console.log(hasNotLogin)
          }
          else{
            hasNotLogin.splice(0,hasNotLogin.length)
          hasNotLogin.push(0)
          }
      },
      destroyed(){
        var hasNotLogin = [1]
      },

      methods:{
        toggleTab:function(tab){
          console.log(tab);
          this.currentTab = tab;
        },
        toLogin:function(){
          console.log("login")
          this.$router.push({
            path: '/Login',
            query: {
            }
          })
        },
        toQuestion:function () {
          this.$router.push({
            path: '/QuestionShow',
            query: {
            }
          })
        },
        toPersonalHome:function () {
          this.$router.push({
            path: '/MyHome',
            query: {
            }
          })
        },
        toQuestionSubmit(){
          if(global.userId===-1)
          {
            this.dialogVisible=true;
          }
          else{
          this.$router.push({
            path: '/QuestionSubmit',
            query: {
            }
          })
        }
        },
        handleClose(done) {
/*          this.$confirm('确认关闭？')
            .then(_ => {
              done();
            })
            .catch(_ => {});
          this.$message({
            type: 'info',
            message: '已取消登陆'
          });*/
        }
      },
  }
</script>

<style scoped>
  @import "../assets/icon/iconfont.css";
  @import "css/guide.css";
  @import "css/buttonBox.css";
  @import "css/searchBar.css";
  #disQue{
    width: 40%;
    height: auto;
    margin-left: 20%;
    display: inline-block;
  }
  #disRight{
    left:62%;
    top:14%;
    width: 300px;
    position: fixed;
  }

  .cright{
    border:1px solid #dee2e6;
    padding:3%;
    margin: 8px;
    background: #FFFFFF;
    outline-color: #bd5151;
    /* border-color: rgba(189,81,81,.8);
    box-shadow: 0 0 8px rgba(181, 185, 189, 0.6);*/
  }

  .container{
    padding: 0;
    margin: 0;
    background: url("../assets/pic/bg/4.png");
    max-width: 100%;
  }
  .rightBtn{
    background:none;
    border: none;
  }
  .rightBtn a:hover{
    color: #bd5151;
  }
  a{
    display: inline-block;
    padding: .5rem 1rem;
    color: #bd5151;
    border: 1px solid transparent;
    border-top-left-radius: .25rem;
    border-top-right-radius: .25rem;
  }
  .active{
    color: #FFFFFF;
    background: #bd5151;
  }
  a:hover{
    /*color: #b35151;*/
    cursor: pointer;
  }
  .nav-tabs{
    border-bottom: 2px solid #bd5151/*dee2e6*/;
  }
  .nav {
    display: -ms-flexbox;
    display: flex;
    -ms-flex-wrap: wrap;
    flex-wrap: wrap;
    padding-left: 0;
    margin-bottom: 0;
    list-style: none;
  }
  ul{
    display: block;
    list-style-type: disc;
    margin-block-start: 1em;
    margin-block-end: 1em;
    margin-inline-start: 0px;
    margin-inline-end: 0px;
    padding-inline-start: 40px;
  }
  .nav-tabs .nav-item{
    margin-bottom: -1px;
  }
  li{
    display: list-item;
    text-align: -webkit-match-parent;
  }
  .nav-tabs{
    border: 1px solid transparent;
    border-top-left-radius: .25rem;
    border-top-right-radius: .25rem;
  }
  .nav-link {
    display: block;
    padding: .5rem 1rem;
  }
  a:-webkit-any-link {
    /*color: -webkit-link;*/
    cursor: pointer;
    /*text-decoration: underline;*/
  }
  .container {
    width: 100%;
    padding-right: 15px;
    padding-left: 15px;
    margin-right: auto;
    margin-left: auto;
  }
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 150px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
</style>
