<template>
  <div>
  <div class="top">
    <ul class="nav" style="padding-left: 9%;">
      <li class="link01">:D&nbsp&nbsp答</li>
      <li class="nav-item">
        <a class="nav-link" @click="toHome"><i class="iconfont">&#xe625;</i>&nbsp&nbsp主页</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" @click="toQuestion"><i class="iconfont">&#xe7bf;</i>&nbsp&nbsp问题</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#"><i class="iconfont">&#xe627;</i>&nbsp&nbsp社区</a>
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
          <button id="triggerBtn" @click="toLogin"><li><a href="#" data-toggle="tooltip" data-placement="bottom" title="登录"><i class="iconfont">&#xe601;</i></a></li></button>
        </div>
        <div v-else>
          <button id="personBtn">
            <div>
              <ul>
                <li style="float:left;margin-top: -2px">
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
  </div>
    <br> <br> <br> <br>
    <div id="disTop">

      <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
        <el-tab-pane label="我的提问" name="first">
          <div >
            <div v-for="content in contents" >
              <div class="contain">
                <div>
                  <img class="cycle" :src="imageUrl" style="float:left;height:100px;width: 100px;padding-top: 12px"/>
                </div>

                <div class="name" style="padding-top: 10px">
                  <h5>{{content.name}}</h5>
                </div>

                <div style="margin-top: 0px">
                  <span>{{content.dateTime}}</span>
                </div>

                <div class="content">
                  <p style="line-height: 50px;">{{content.con}}</p>
                </div>
              </div>
            </div>
            <!--动态 end-->
          </div>
        </el-tab-pane>
        <el-tab-pane label="我的回答" name="second">我的回答</el-tab-pane>
        <el-tab-pane label="我的收藏" name="third">我的收藏</el-tab-pane>
        <el-tab-pane label="我的赞赏" name="fourth">我的赞赏</el-tab-pane>
      </el-tabs>

    </div>
    <div id="disLeft">
      <!--卡片1-->
      <div class="cright">
        <el-upload
          class="avatar-uploader"
          action="http://query.liublack.cn/qs/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <img v-if="imageUrl" :src="imageUrl" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <h4>&nbsp;&nbsp;{{myName}}</h4>
      <el-collapse v-model="activeNames" @change="handleChange">
        <el-collapse-item title="我的积分" name="1">
          <div>积分:{{this.score}}</div>
        </el-collapse-item>
        <el-collapse-item title="个人简介" name="2">
          <div>这个同学很懒，什么也木有写。
            <el-button type="text"><i class="iconfont">&#xe628;</i></el-button>
          </div>
        </el-collapse-item>
        <el-collapse-item title="个性签名" name="3">
          <div>这个同学很懒，什么也木有写。<el-button type="text"><i class="iconfont">&#xe628;</i></el-button></div>
        </el-collapse-item>
        <el-collapse-item title="兴趣爱好" name="4">
          <div>爱好学习<el-button type="text"><i class="iconfont">&#xe628;</i></el-button></div>
        </el-collapse-item>
      </el-collapse>
        <a class="card-link" @click="toAlterPsw" style="cursor: pointer">点击修改密码>></a>
        <a class="card-link" @click="toAlterInfo" style="cursor: pointer">点击修改个人资料>></a>
<!--        <el-link :underline="false" type="primary" @click="toAlterPsw">点击修改密码>></el-link>-->
<!--        <el-link :underline="false" type="primary" @click="toAlterInfo">点击修改个人资料>></el-link>-->
      </div>
    </div>
  </div>
</template>

<script>
  import global from './global.vue'
  import util from '@/util.js'
  var hasNotLogin = [1]
  var content=[]

    export default {
        name: "MyHome",
      data(){
        return{
          score:'',
          imageUrl:'',
          circleUrl: global.photo,
          hasNotLogin:hasNotLogin,
          myId:global.userId,
          myName:global.name,
          activeNames: ['1'],
          contents:content,
          activeName: 'first'
        }
      },

      created(){
        console.log("create"+global.userId)
        if(global.userId==-1){
          console.log(hasNotLogin)
        }
        else{
          hasNotLogin.splice(0,hasNotLogin.length)
          hasNotLogin.push(0)
        }
       this.$axios(
          {
            method:'get',
            url:global.host+"/uploadphoto",
            params:{
              email:global.email,
            }
          }).then(res =>{
            console.log(res)
          this.imageUrl=res.data.trim()
         console.log("-------touxiang------")
          console.log(this.imageUrl)

        }).catch(e =>{

        })

        this.$axios(
          {
            method:'post',
            url:global.host+"/ReturnQuestion",
            params:{
              mail:global.email
            }
          }).then(res =>{
          console.info(res)
          for (var i = 0; i < res.data.question.length; i++) {
            var l = {
              //img: res.data.question[i].photo,
              name: this.userName,
              dateTime: util.formatTime(res.data.question[i].time),
              con: decodeURI(res.data.question[i].content),
              //con_image:res.data.question[i].con_image,
              // p:res.data.question[i].p,
              // d:res.data.question[i].d,
              //z:res.data.question[i].z,
            }
            content.push(l)
          }
        }).catch(e =>{
          console.info(e)
        })

        this.$axios(
          {
            method:'get',
            url:global.host+"/ReturnInformation",
            params:{
              email:global.email
            }
          }).then(res =>{
          console.log(res)
          this.score=res.data.score
        }).catch(e =>{
          console.info(e)
        })


      },

      destroyed(){
        content.splice(0,content.length)
      },

      methods:{
        toLogin:function(){
        },
        toQuestion:function () {
          this.$router.push({
            path: '/QuestionShow',
            query: {
            }
          })
        },
        toHome:function () {
          this.$router.push({
            path: '/',
            query: {
            }
          })
        },
        toAlterPsw:function(){
          this.$router.push({
            path: '/AlterPsw',
            query: {
            }
          })
        },
        toAlterInfo:function() {
          this.$router.push({
            path: '/AlterInfo',
            query: {
            }
          })
        },
        handleChange(val) {
          console.log(val);
        },
        handleClick(tab, event) {
          console.log(tab, event);
        },
        handleAvatarSuccess(res, file) {
          this.imageUrl = URL.createObjectURL(file.raw);
          var uri='http://query.liublack.cn/qs'+res[0].uri
          console.log(uri)
          console.log(res[0].uri)
          this.$axios(
            {
              method:'post',
              url:global.host+"/uploadphoto",
              params:{
                email:global.email,
                uri:uri
              }
            }).then(res =>{
              console.log(res)
            if(res.data.trim()=='success')
            {
              this.$alert('上传头像成功', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                  global.photo=uri
                }
              });
            }
            else {
              this.$alert('上传头像失败', '提示', {
                confirmButtonText: '确定',
                callback: action => {

                }
              });
            }

          }).catch(e =>{
            console.log(1111)
            console.info(e)
          })
        },

        beforeAvatarUpload(file) {
          const isJPG = file.type === 'image/jpeg';
          const isLt2M = file.size / 1024 / 1024 < 2;

          if (!isJPG) {
            this.$message.error('上传头像图片只能是 JPG 格式!');
          }
          if (!isLt2M) {
            this.$message.error('上传头像图片大小不能超过 2MB!');
          }
          return isJPG && isLt2M;
        },

      }
    }
</script>

<style scoped>
  @import "../assets/icon/iconfont.css";
  @import "css/guide.css";
  @import "css/buttonBox.css";
  @import "css/searchBar.css";
  #disTop{
    width: 65%;
    height: 500px;
    margin-left: 20%;
    margin-top:10px;
    display: inline-block;
    /*background: #FFFFFF;*/
  }
  #disLeft{
    left:3%;
    top:8%;
    width: 280px;
    position: fixed;
  }
  .cright{
    border:1px solid #dee2e6;
    padding:3%;
    margin: 8px;
    background: #FFFFFF;
    outline-color: #bd5151;
    height: auto;
    padding-bottom: 5%;
    /* height: 400px;
    border-color: rgba(189,81,81,.8);
    box-shadow: 0 0 8px rgba(181, 185, 189, 0.6);*/
  }
  .el-tabs--border-card>.el-tabs__header .el-tabs__item.is-active {
    color: #bd5151;
    background-color: #FFF;
    border-right-color: #DCDFE6;
    border-left-color: #DCDFE6;
  }
  .el-tabs--border-card>.el-tabs__header .el-tabs__item+.el-tabs__item, .el-tabs--border-card>.el-tabs__header .el-tabs__item:first-child {
    margin-left: -1px;
  }
  .el-tabs--border-card>.el-tabs__header .el-tabs__item.is-active {
    color: #bd5151;
    background-color: #FFF;
    border-right-color: #DCDFE6;
    border-left-color: #DCDFE6;
  }
  .el-tabs--border-card>.el-tabs__header .el-tabs__item+.el-tabs__item, .el-tabs--border-card>.el-tabs__header .el-tabs__item:first-child {
    margin-left: -1px;
  }
  .el-tabs--border-card>.el-tabs__header .el-tabs__item {
    -webkit-transition: all .3s cubic-bezier(.645,.045,.355,1);
    transition: all .3s cubic-bezier(.645,.045,.355,1);
    border: 1px solid transparent;
    margin-top: -1px;
    color: #909399;
  }
  .el-tabs--border-card>.el-tabs__header .el-tabs__item {
    -webkit-transition: all .3s cubic-bezier(.645,.045,.355,1);
    transition: all .3s cubic-bezier(.645,.045,.355,1);
    border: 1px solid transparent;
    margin-top: -1px;
    color: #909399;
  }

  el-tabs__item:active {
    color: #bd5151;
  }
  .el-tabs__item {
    padding: 0 20px;
    height: 40px;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    line-height: 40px;
    display: inline-block;
    list-style: none;
    font-size: 14px;
    font-weight: 500;
    color: #303133;
    position: relative;
  }
  .el-tabs__item {
    padding: 0 20px;
    height: 40px;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    line-height: 40px;
    display: inline-block;
    list-style: none;
    font-size: 14px;
    font-weight: 500;
    color: #303133;
    position: relative;
  }
  .avatar-uploader{
    border: 1px solid #000;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 60px;
    height: 60px;
    border-radius: 50%;
    margin-left: 12%;
    margin-top: 10px;
  }
  .avatar-uploader{
    border-color: #bd5151;
  }
  .avatar-uploader-icon {
    font-size: 20px;
    color: #8c939d;
    width: 60px;
    height: 60px;
    line-height: 60px;
    text-align: center;
  }
  .avatar {
    width: 60px;
    height: 60px;
    display: block;
  }
  .contain{
    padding-top: 0px;
    margin-left: 5%;
    width: 60%;
    height: auto;
    border-bottom: 2px solid #888888;

  }
  .cycle{
    width: 60px;
    height: 60px;
    border-radius: 50%;
    margin-left: 8%;
    padding-top: 10px;
  }

  .name{
    font-size: 20px;
    text-align:left;
    padding-top: 10px;
  }
  .content{
    width: 60%;
    height: auto;
    margin-left: 180px;
  }
</style>
