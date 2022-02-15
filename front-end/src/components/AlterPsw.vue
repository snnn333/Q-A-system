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
            <button id="personBtn" @click="toMyHome">
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

      <div style="margin-top: 50px">
        <div class="reg_form" >
          <el-steps :active="active" align-center finish-status="success">
            <el-step title="步骤 1" description="请输入旧密码"></el-step>
            <el-step title="步骤 2" description="请输入新密码"></el-step>
            <el-step title="步骤 3" description="请确定新密码"></el-step>
          </el-steps>
          <br>
          <div v-if="active==0">
            <input type="text"  class="qxs-ic_password qxs-icon" v-model="oldpsw" v-if="this.seen.oldpwdType" placeholder="旧密码" style="font-size: 20px;padding-top: 30px">
            <input  type="password" class="qxs-ic_password qxs-icon" style="font-size: 20px;padding-top: 30px" placeholder="旧密码" v-model="oldpsw" v-else>
            <img :src="see?seeImg:unseeImg" @click="changeoldType" style="height: 20px;width: 20px;cursor: pointer">
          </div>

          <div v-if="active==1">
            <input type="text"  class="qxs-ic_password qxs-icon"  placeholder="新密码" v-if="this.seen.newpwdType" v-model="newpsw" style="font-size: 20px;padding-top: 30px">
            <input  type="password" class="qxs-ic_password qxs-icon" style="font-size: 20px;padding-top: 30px" placeholder="新密码" v-model="newpsw" v-else>
            <img :src="seeTwo?seeImg:unseeImg" @click="changenewType" style="height: 20px;width: 20px;cursor: pointer">
          </div>

          <div v-if="active==2">
            <input type="text"  class="qxs-ic_password qxs-icon"  placeholder="确定密码" v-if="this.seen.confirmpswType" v-model="confirmpsw" style="font-size: 20px;padding-top: 30px">
            <input  type="password" class="qxs-ic_password qxs-icon" style="font-size: 20px;padding-top: 30px" placeholder="确定密码" v-model="confirmpsw" v-else>
            <img :src="seeThree?seeImg:unseeImg" @click="confirmnewType" style="height: 20px;width: 20px;cursor: pointer">
          </div>

          <!--<button class="login_btn el-button el-button&#45;&#45;primary is-round" type="primary" round>登录</button>-->
          <br><br>
          <el-button type="primary" @click="before"  style="margin-left: 10%">上一步</el-button>
          <el-button type="primary" @click="nextone" style="margin-left: 62%" v-if="active==0">下一步</el-button>
          <el-button type="primary" @click="nexttwo" style="margin-left: 62%" v-if="active==1">下一步</el-button>
          <el-button type="primary" @click="confirm" style="margin-left: 62%" v-if="active==2">确定提交</el-button>
          <br><br><br>
          <!--动态 end-->
        </div>
      </div>

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
        <h4>{{myName}}</h4>
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
      </div>
    </div>
  </div>
</template>

<script>
  import global from './global.vue'
  var hasNotLogin = [1]
  export default {
    name: "AlterPsw",
    data(){
      return{
        imageUrl:'',
        score:'',
        circleUrl: global.photo,
        hasNotLogin:hasNotLogin,
        myId:global.userId,
        myName:global.name,
        activeNames: ['1'],
        active: 0,
        userName:'',
        message: '',
        oldpsw: '',
        newpsw:'',
        confirmpsw:'',
        rsaoldPassword:'',
        rsanewPassword:'',
        rsaconPassword:'',
        phone:'',
        is_currect:'',
        error:{
          oldpsw: '',
          newpsw:'',
          confirmpsw:'',
          null:''
        },
        seen:{
          oldpwdType: false,
          newpswType:false,
          confirmpswType:false,
        },
        see:'',
        seeTwo:'',
        seeThree:'',
        seeImg:require('../assets/see.png'),
        unseeImg:require('../assets/unsee.png'),
      }
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
      this.$axios(
        {
          method:'get',
          url:global.host+"/uploadphoto",
          params:{
            email:global.email,
          }
        }).then(res =>{
        this.imageUrl=res.data.trim()
        console.log(this.imageUrl)

      }).catch(e =>{
        console.log(1111)
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
      var hasNotLogin = [1]
    },
    methods:{

      toQuestion:function () {
        this.$router.push({
          path: '/QuestionShow',
          query: {
          }
        })
      },
      toMyHome:function(){
        this.$router.push({
          path: '/MyHome',
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
      toHome:function () {
        this.$router.push({
          path: '/',
          query: {
          }
        })
      },
      handleChange(val) {
        console.log(val);
      },

      handleAvatarSuccess(res, file) {
        this.imageUrl = URL.createObjectURL(file.raw);
        var uri=global.host+res[0].uri
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
      before(){
        if(this.active==0)
        {
          this.$alert('没有上一步', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
          return false
        }
        else {
          this.active--
        }
      },
      nextone() {
        if(!this.oldpsw){
          this.$alert('请输入旧密码！', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
          return false
        }
        else{
          this.active=1
        }
      },

      nexttwo() {
        let pPattern = /^[a-zA-Z0-9]{6,20}$/
        if(!this.oldpsw){
          this.$alert('请输入新密码！', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
          return false
        }
        else if(!pPattern.test(this.newpsw))
        {
          this.$alert('密码至少6位,可包括大写字母，小写字母，数字', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
          return false
        }
        else{
          this.active=2
        }

      },
      goBack() {
        console.log('go back');
      },


      success(){
        this.oldpsw=''
        this.newpsw=''
        this.confirmpsw=''
        this.error.oldpsw=''
        this.error.null = ''
        this.error.newpsw = ''
        this.error.confirmpsw = ''
        this.is_currect=''
        this.see=''
        this.seeTwo=''
        this.seeThree=''
      },

      changeoldType(){
        this.see=!this.see
        this.seen.oldpwdType=!this.seen.oldpwdType
      },

      changenewType(){
        this.seeTwo=!this.seeTwo
        this.seen.newpwdType=!this.seen.newpwdType
      },
      confirmnewType(){
        this.seeThree=!this.seeThree
        this.seen.confirmpswType=!this.seen.confirmpswType
      },

      to_changeInfo(){
        this.$router.push({path:'/changeInfo'})
      },
      to_PersonalHome(){
        this.$router.push({path:'/PersonalHome'})
      },
      alterpsw(){
        this.$router.push({path:'/psw'})
      },

      con(){


      },
      confirm(){
        this.error.oldpsw=''
        this.error.null = ''
        this.error.newpsw = ''
        this.error.confirmpsw = ''

        if(!this.confirmpsw){
          this.$alert('请确定新密码！', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
          return false
        }

        if(this.newpsw!=this.confirmpsw)
        {
          this.$alert('两次密码不一致', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
          return false
        }

        this.$axios(
          {
            method:'get',
            url:global.host+"/rsaPassword",
            params:{
            }
          }).then(res=>{
          console.log(res)
          let encryptor=new JSEncrypt()
          let publicKey=res.data
          encryptor.setPublicKey(publicKey)
          this.rsaoldPassword=encryptor.encrypt(this.oldpsw)
          this.rsanewPassword=encryptor.encrypt(this.newpsw)
          console.log(this.rsaoldPassword)
          console.log(this.rsanewPassword)
          this.$axios(
            {
              method:'post',
              url:global.host+"/AlterPsw",
              params:{
                email:global.email,
                oldpsw:this.rsaoldPassword,
                newpsw: this.rsanewPassword,
              }
            }).then(res =>{
            console.log(res)
            if(res.data.trim()=="successfully")
            {
              this.$alert('修改密码成功！', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                  this.success()
                  this.active=0
                  this.$router.push({
                    path: '/Login',
                    query: {
                    }
                  })
                }
              });
            }
            if(res.data.trim()=="failed")
            {
              this.$alert('修改密码失败！请确定旧密码是否正确！', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                }
              });
            }
          }).catch(e =>{
            console.info(e)
          })

        }).catch(e =>{
          console.info(e)
          console.log('连接失败')
        })
      }


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
    /* border-color: rgba(189,81,81,.8);
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
  .el-tabs__item.is-active {
    color: #bd5151;
  }
  .el-tabs__item.is-active {
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

  .qxs-ic_password {
    background: url("../assets/password.png") no-repeat;
    background-size: 30px 30px;
    background-position: 3%;
  }

  .login_btn {
    width: 100px;
    height: 40px;
    margin-left:58%;
    font-size: 18px;
    background-color: white;
    filter: brightness(1.4);
    border-radius: 15%;
    outline-color: #bd5151;
  }

  .qxs-icon {
    height: 40px;
    width: 60%;
    margin-left: 12%;
    padding-bottom: 30px;
    padding-left: 10%;
    border: 0;
    border-bottom: solid 2px lavender;
  }

  .reg_form {
    width: 1200px;
    margin-left: 0;
    padding-top: 25px;
    background-color: white;

  }
</style>
