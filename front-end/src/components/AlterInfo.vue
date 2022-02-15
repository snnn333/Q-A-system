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
      <div style="margin-top: 0">
        <div class="reg_form" >
          <input type="text"  class="qxs-ic_user qxs-icon"  placeholder="用户名" v-model="userName" style="font-size: 20px;padding-top: 30px">
          <span v-if="error.userName" class="err-msg">{{error.userName}}</span>
          <input type="text"  class="qxs-ic_email qxs-icon"  placeholder="邮箱" v-model="email" style="font-size: 20px;padding-top: 30px">
          <span v-if="error.email" class="err-msg">{{error.email}}</span>
          <input type="text"  class="qxs-ic_age qxs-icon"  placeholder="年龄" v-model="age" style="font-size: 20px;padding-top: 30px">
          <span v-if="error.age" class="err-msg">{{error.age}}</span>
          <input type="text"  class="qxs-ic_phone qxs-icon"  placeholder="电话" v-model="phone" style="font-size: 20px;padding-top: 30px">
          <span v-if="error.phone" class="err-msg">{{error.phone}}</span>
          <!--<button class="login_btn el-button el-button&#45;&#45;primary is-round" type="primary" round>登录</button>-->
          <br><br>
          <el-button type="primary" @click="confirm" style="font-size: 20px; margin-left: 40%;" >确定修改</el-button>
          <br><br>
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
    name: "AlterInfo",
    data(){
      return{
        imageUrl:'',
        score:'',
        circleUrl: global.photo,
        hasNotLogin:hasNotLogin,
        myId:global.userId,
        myName:global.name,
        activeNames: ['1'],
        message: '',
        userName: '',
        email:'',
        age:'',
        phone:'',
        error:{
          userName:'',
          email:'',
          age:'',
          phone:'',
          null:''
        }
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
        this.imageUrl=res.data.trim()
        console.log(this.imageUrl)

      }).catch(e =>{
        console.log(1111)
        console.info(e)
      })

      this.$axios(
        {
          method:'get',
          url:global.host+"/AlterInformation",
          params:{
            email:global.email
          }
        }).then(res =>{
        console.log(res)
        this.userName=decodeURI(res.data.username)
        this.email=res.data.mail
        this.age=res.data.age
        this.phone=res.data.phone
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
      var hasNotLogin = [1]
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
      toMyHome:function(){
        this.$router.push({
          path: '/MyHome',
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
          if(res.data.trim()=='success')
          {
            this.$alert('上传头像成功', '提示', {
              confirmButtonText: '确定',
              callback: action => {
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
      success(){

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

      confirm(){
        this.error.null = ""
        this.error.userName=""
        this.error.email=""
        this.error.age=''
        this.error.phone=''
        if(!this.userName){
          this.error.userName='用户名不能为空'
          return false
        }
        else{
          this.error.userName=''
        }


        if(!this.email){
          this.error.email='邮箱不能为空'
          return false
        }
        else{
          this.error.email=''
        }

        if(!this.age){
          this.error.age='年龄不能为空'
          return false
        }
        else{
          this.error.age=''
        }

        if(!this.phone){
          this.error.phone='电话不能为空'
          return false
        }
        else{
          this.error.phone=''
        }

        let uPattern = /^[\u4e00-\u9fa5A-Za-z0-9-_]+$/
        if(!uPattern.test(this.userName))
        {
          this.$alert('用户名不合法', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
          return false
        }

        let ePattern = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/
        if(!ePattern.test(this.email))
        {
          this.$alert('邮箱不合格', '提示', {
            confirmButtonText: '确定',
            callback: action => {

            }
          });
          return false
        }
        let agePattern=/^[0-9]*$/
        if(!agePattern.test(this.age))
        {
          this.$alert('年龄只能包含数字', '提示', {
            confirmButtonText: '确定',
            callback: action => {

            }
          });
          return false
        }

        this.$axios(
          {
            method:'post',
            url:global.host+"/AlterInformation",
            params:{
              username:encodeURI(this.userName),
              email:this.email,
              age:this.age,
              phone:this.phone

            }
          }).then(res =>{
          console.log(res)
            console.log("in")
            this.userName = res.data.username
            this.email = res.data.mail
            this.age = res.data.age
            this.phone = res.data.phone
            global.email=this.email
            global.name=this.userName
          this.$alert('修改成功！', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
        }).catch(e =>{
         console.log("catch")
          this.$alert('服务器繁忙！请稍后重试！', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
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
  .qxs-ic_user {
    background: url("../assets/user.png") no-repeat;
    background-size: 30px 30px;
    background-position: 3%;
  }



  .qxs-ic_phone {
    background: url("../assets/phone.png") no-repeat;
    background-size: 30px 30px;
    background-position: 3%;
  }

  .qxs-ic_age {
    background: url("../assets/age.png") no-repeat;
    background-size: 30px 30px;
    background-position: 3%;
  }

  .qxs-ic_email{
    background: url("../assets/email.png") no-repeat;
    background-size: 30px 30px;
    background-position: 3%;
  }

  .login_btn {
    width: 150px;
    height: 60px;
    margin-top: 30px;
    margin-left: 40%;
    font-size: 20px;
    background-color: white;
    filter: brightness(1.4);
    border-radius: 15%;
  }

  .qxs-icon {
    height: 40px;
    width: 60%;
    margin-left: 12%;
    padding-bottom: 30px;
    padding-left: 10%;
    border: 0;
    border-bottom: solid 3px lavender;
  }

  .reg_form {
    width: 1200px;
    margin-left: 0px;
    padding-top: 25px;
    background-color: white;
  }

  .err-msg{
    width: 50px;
    background-color:white;
    font-size: 20px;
  }
</style>
