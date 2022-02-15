<template>
  <div  :style="bg" style="height: 100%">

    <div class="contain">
      <li class="link01" style="margin-left: 30%">Q/A SYSTEM</li>
      <h2 style="text-align: center">亲爱的用户，感谢注册Q/A账号</h2>
      <br>
      <a style="font-size: 18px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本系统为大二下Java实训的作业，由厦门大学软件学院</a>
      <br>
      <a style="font-size: 18px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;软件工程专业五名同学完成，非开发人员未经允许禁止将</a>
      <br>
      <a style="font-size: 18px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本系统用于商业用途!</a>
      <br><br><br>
      <img src="../assets/3.jpg" style="height:200px;width: 200px;padding-left: 30%"/>
      <br><br><br>
      <div style="text-align: center">
      <span style="font-size: 20px;">请点击</span>
      <span style="color: #000099;font-size: 20px;cursor: pointer;text-decoration: underline" @click="to_con">确定</span>
      <span style="font-size: 20px">激活账号</span>
        <br><br><br>
    </div>
    </div>
  </div>
</template>a

<script>
  import global from './global.vue'

    export default {

      name: "confirmRegister",
      data()
      {
        return{
          bg: {
            backgroundImage: "url(" + require("../assets/login_bg.png") + ")",
            backgroundRepeat: "no-repeat",
            backgroundSize: "100% 100%",
          },
        }
      },

      methods: {
        to_con() {
          this.$axios(
            {
              method:'post',
              url:global.host+"/confirmRegister",
              params:{
                email:global.email,
              }
            }).then(res =>{
              console.log(res)
            if(res.data.trim()=='success')
            {
              this.$alert('账号已经激活，可按确定前往登录界面！', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                  this.$router.push({path:'/Login'})
                }
              });
            }
            else{
              this.$alert('服务器繁忙！请稍后重试！', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                  this.$router.push({path:'/Login'})
                }
              });
            }

          }).catch(e =>{
            console.info(e)
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

    .contain{
      position: absolute;
      top:20%;
      width: 520px;
      margin-left: 37%;
      background-color: white;
      padding-top: 25px;
      box-shadow:5px 5px 5px grey;
    }
</style>
