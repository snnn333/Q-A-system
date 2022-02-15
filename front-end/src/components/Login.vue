<template>
  <div  :style="bg" style="height: 100%">

      <div class="login_form">
        <a class="link01" style="margin-left: 32%;font-size:55px;">:D&nbsp;&nbsp;答</a>
        <br>
        <input type="text"  class="qxs-ic_email qxs-icon"  placeholder="邮箱" v-model="userName" style="font-size: 20px;padding-top: 30px">
        <span v-if="error.userName" class="err-msg">{{error.userName}}</span>

        <input type="text"  class="qxs-ic_password qxs-icon"  placeholder="密码" v-if="seen.pswType" v-model="password" style="font-size: 20px;padding-top: 30px">
        <input  type="password" class="qxs-ic_password qxs-icon" style="font-size: 20px;padding-top: 30px" placeholder="密码" v-model="password" v-else>
        <img :src="see?seeImg:unseeImg" @click="changeType" style="height: 20px;width: 20px;cursor: pointer">
        <span v-if="error.password" class="err-msg">{{error.password}}</span>
      <!--<button class="login_btn el-button el-button&#45;&#45;primary is-round" type="primary" round>登录</button>-->
        <br>
        <el-button type="danger" class="login_btn" @click="login" style="font-size: 20px">登录</el-button>
        <div style="margin-top: 30px;padding-bottom: 10%">
          <span style="color: #000099;margin-left: 20%;font-size: 20px;cursor: pointer" @click="to_reg">注册</span><span style="margin-left: 40%;color: #A9A9AB;font-size: 20px;cursor: pointer" @click="to_reset">忘记密码？</span>
        </div>

    </div>
  </div>
</template>

<script>
  import global from './global.vue'
  import { JSEncrypt } from 'jsencrypt'

  export default {
    data() {
      return {
        bg: {

        },
        userName: '',
        password: '',
        rsaPassword:'',
        error:{
          userName:'',
          password:'',
          null:''
        },
        seen:{
          pswType:false,
        },
        see:'',
        seeImg:require('../assets/see.png'),
        unseeImg:require('../assets/unsee.png'),
      }
    },
    /*created () {
      if(JSON.parse( localStorage.getItem('user')) && JSON.parse( localStorage.getItem('user')).userName){
        this.userName = JSON.parse( localStorage.getItem('user')).userName;
        this.password = JSON.parse( localStorage.getItem('user')).password;
      }
    },*/

    methods: {

      changeType(){
        this.see=!this.see
        this.seen.pswType=!this.seen.pswType
      },
      to_reset(){
        if(!this.userName)
        {
          this.$alert('请先填写邮箱再忘记密码！', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });

        }
        else {
          this.$alert('确定忘记密码？', '提示', {
            confirmButtonText: '确定',
            callback: action => {
              this.$axios(
                {
                  method:'post',
                  url:global.host+"/SendEmailtwo",

                  params:{
                    email:this.userName,
                  }
                }).then(res=>{
                  global.email=this.userName
                console.log(res)
                this.$alert('邮件已经发送！', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                 }
               });
              })

            }
          });
        }
      },

      to_reg(){
        this.$router.push({path:'/register'})
      },

      login() {
        this.error.null=''
        if(!this.userName){
          this.error.userName='请输入邮箱'
          return false
        }
        else{
          this.error.userName=''
        }

        if(!this.password){
          this.error.password='请输入密码'
          return false
        }
        else{
          this.error.password=''
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
          let publicKey=res.data   //得到公钥
          encryptor.setPublicKey(publicKey)
          this.rsaPassword=encryptor.encrypt(this.password)
          console.log(this.rsaPassword)
          this.$axios(
            {
              method:'post',
              url:global.host+"/loginMail",
              params:{
                mail:this.userName,
                password:this.rsaPassword
              }
            }).then(res =>{
            console.log(res)
            if(res.data.role.trim()== "user")
            {
              global.email=this.userName
              global.userId=res.data.userId
              global.name=decodeURI(res.data.name)
              global.photo=res.data.photo
              console.log(global.userId)
              console.log(global.name)
              console.log(global.photo)
              this.$alert('登录成功！请按确定前往主界面', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                  this.$router.push({path:'/QuestionShow'})
                }
              });
            }
            else if(res.data.role.trim()== "manager")
            {
              global.email=this.userName
              global.userId=res.data.userId
              global.name=decodeURI(res.data.name)
              global.photo=res.data.photo
              console.log(global.userId)
              this.$alert('管理员登录成功！请按确定前往管理界面', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                  this.$router.push({path:'/OperatorUser'})
                }
              });
            }
            else if(res.data.role.trim()== "forbidden")
            {
              this.$alert('账号未激活，禁止登陆！', '提示', {
                confirmButtonText: '确定',
                callback: action => {

                }
              });
              return false
            }
            else
            {
              this.$alert('邮箱或密码错误！', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                }
              });
              return false
            }
          }).catch(e =>{
            console.info(e)
            console.log('连接失败')
          })
        }).catch(e =>{
          console.log("oooo")
        })


      }

    }
  }

</script>

<style>
  @import "../components/css/guide.css";

  .login_form {
    position: absolute;
    top:20%;
    width: 520px;
    margin-left: 37%;
    background-color: white;
    padding-top: 30px;
    box-shadow: 0 0 8px rgba(181, 185, 189, 0.6);
  }
  .qxs-ic_user {
    background: url("../assets/user.png") no-repeat;
    background-size: 30px 30px;
    background-position: 3%;
  }

  .qxs-ic_password {
    background: url("../assets/password.png") no-repeat;
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

  .bg{
    background:url("../assets/login_bg.png") center center;
    backgroundSize:"100% 100%";
  }

  .login_logo {
    height: 100%;
  }

  .login_btn {
/*    width: 100px;
    height: 50px;*/
    margin-top: 30px;
    margin-left: 40%;
   /* font-size: 16px;
    filter: brightness(1.4);
    border-radius: 15%;
    outline:none;*/
  }

  body {
    padding: 0;
    margin:0;
    font-family: "Microsoft YaHei UI Light";
  }

  .outer_label {
    position: relative;
    left: 0;
    top: 0;
    width: 100%;
    height: 100px;
    background-color:#2154FA;
    text-align: center;
    filter: brightness(1.4);
  }

  .inner_label {
    position: absolute;
    left:0;
    right: 0;
    bottom: 0;
    top:0;
    margin: auto;
    height: 50px;
  }

  .qxs-icon {
    height: 40px;
    width: 40%;
    margin-left: 25%;
    padding-bottom: 10px;
    padding-left: 10%;
    border: 0;
    border-bottom: solid 2px lavender;
    outline-color: #bd5151;
  }
  .err-msg{
    width: 50px;
    background-color:white;
    font-size: 20px;
  }

</style>
