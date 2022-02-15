<template>

  <div :style="bg" style="height: 100%">
    <div class="reg_form" >
      <li class="link01" style="margin-left: 30%">Q/A SYSTEM</li>
      <br> <br>
      <a style="font-size: 18px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;亲爱的用户，确定修改密码请在下方的输入框输入新密码！</a>
      <br>
      <a style="font-size: 18px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;修改成功后，您将可以用新密码登录我们的系统，继续您愉</a>
      <br>
      <a style="font-size: 18px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;快的旅程！</a>
      <br>
      <img src="../assets/3.jpg" style="height:200px;width: 200px;padding-left: 30%"/>
      <input type="text"  class="qxs-ic_password qxs-icon"  placeholder="新密码" v-if="this.seen.newpwdType" v-model="newpsw" style="font-size: 20px;padding-top: 30px">
      <input  type="password" class="qxs-ic_password qxs-icon" style="font-size: 20px;padding-top: 30px" placeholder="新密码" v-model="newpsw" v-else>
      <img :src="seeTwo?seeImg:unseeImg" @click="changenewType" style="height: 20px;width: 20px;cursor: pointer">
      <span v-if="error.newpsw" class="err-msg">{{error.newpsw}}</span>

      <input type="text"  class="qxs-ic_password qxs-icon"  placeholder="确定密码" v-if="this.seen.confirmpswType" v-model="confirmpsw" style="font-size: 20px;padding-top: 30px">
      <input  type="password" class="qxs-ic_password qxs-icon" style="font-size: 20px;padding-top: 30px" placeholder="确定密码" v-model="confirmpsw" v-else>
      <img :src="seeThree?seeImg:unseeImg" @click="confirmnewType" style="height: 20px;width: 20px;cursor: pointer">
      <span v-if="error.confirmpsw" class="err-msg">{{error.confirmpsw}}</span>
      <!--<button class="login_btn el-button el-button&#45;&#45;primary is-round" type="primary" round>登录</button>-->
      <br><br><br>
      <span v-if="error.null" class="err-msg" style="margin-left: 40%">{{error.null}}</span>
      <button class="login_btn" @click="confirm" style="font-size: 20px;margin-left: 37%;" >确定修改</button>
      <!--动态 end-->
    </div>

  </div>
    
</template>

<script>

  import global from './global.vue'

    export default {
        name: "resetPsw",
      data()
      {
        return{
          userName:'',
          imageUrl: '',
          message: '',
          bg: {
            backgroundImage: "url(" + require("../assets/login_bg.png") + ")",
            backgroundRepeat: "no-repeat",
            backgroundSize: "100% 100%",
          },
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

      methods:{
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

        confirm(){
          this.error.oldpsw=''
          this.error.null = ''
          this.error.newpsw = ''
          this.error.confirmpsw = ''


          if(!this.newpsw){
            this.error.newpsw='请输入新密码'
            return false
          }
          else{
            this.error.newpsw=''
          }

          if(!this.confirmpsw){
            this.error.confirmpsw='请确定新密码'
            return false
          }
          else{
            this.error.confirmpsw=''
          }

          let pPattern = /^[a-zA-Z0-9]{6,20}$/
          if(!pPattern.test(this.newpsw))
          {
            this.$alert('密码至少6位,可包括大写字母，小写字母，数字', '提示', {
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
                this.success()
              }
            });
            return false
          }

          this.$axios(
            {
              method:'get',
              url:"http://localhost:8082/test/rsaPassword",
              params:{
              }
            }).then(res=>{
            console.log(res)
            let encryptor=new JSEncrypt()
            let publicKey=res.data
            encryptor.setPublicKey(publicKey)
            this.rsanewPassword=encryptor.encrypt(this.newpsw)
            console.log(this.rsaoldPassword)
            console.log(this.rsanewPassword)
            this.$axios(
              {
                method:'post',
                url:"http://localhost:8082/test/ForgetPassword",
                params:{
                  email:global.email,
                  password: this.rsanewPassword,
                }
              }).then(res =>{
              console.log(res)
              if(res.data.trim()=="successfully")
              {
                this.$alert('修改密码成功！', '提示', {
                  confirmButtonText: '确定',
                  callback: action => {
                    this.success()
                  }
                });
              }
              if(res.data.trim()=="error")
              {
                this.$alert('修改密码失败！请稍后重试！', '提示', {
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
  @import "http://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css";
  @import "../components/css/buttonBox.css";
  @import "../assets/icon/iconfont.css";
  @import "../components/css/searchBar.css";
  @import "../components/css/top.css";


  .qxs-ic_password {
    background: url("../assets/password.png") no-repeat;
    background-size: 30px 30px;
    background-position: 3%;
  }




  .login_btn {
    width: 150px;
    height: 60px;
    margin-top: 30px;
    font-size: 20px;
    background-color: white;
    filter: brightness(1.4);
    border-radius: 15%;
  }

  .qxs-icon {
    height: 40px;
    width: 60%;
    margin-left: 5%;
    padding-bottom: 30px;
    padding-left: 10%;
    border: 0;
    border-bottom: solid 3px lavender;
  }

  .reg_form {
    position: fixed;
    top:15%;
    width: 30%;
    height: auto;
    margin-left: 36%;
    padding-top: 25px;
    background-color: white;
    box-shadow:5px 5px 5px grey;
  }

  .err-msg{
    width: 50px;
    background-color:wheat;
    font-size: 18px;
  }

</style>
