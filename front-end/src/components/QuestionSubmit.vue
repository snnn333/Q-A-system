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
                <div @click="toMyHome">
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

    <div>
      <div class="QuestionContent" >
<!--        <h4 style="font-size: 25px">请为你的问题添加标题</h4>-->
        <div class="input-group mb-3" >
          <input type="text" class="form-control" v-model="title" placeholder="请输入您的问题标题"  id="title" name="title" >
        </div>
        <!--  <input type="text"  name="title">-->
        <input-fielda class="input" :toolbar = "toolbar" :content="content" @change="changeContent"> </input-fielda>
      </div>
      <div class="options" >

      <h4>请选择问题类别</h4>
      <single-select v-bind:optionsdata="single.originOptions" v-bind:selecteddata="single.selected" v-on:selected="singleCallback" ></single-select>

      <h4>请为你的问题添加标签</h4>
      <custom-multiple-input  v-bind:selecteddata="labels" v-on:inputed="labelsCallback" noDataMess="例如：王者荣耀"></custom-multiple-input>
        <h4>请为你的问题添加悬赏积分</h4>
        <single-select v-bind:optionsdata="scores.scoreOptions" v-bind:selecteddata="scores.selected" v-on:selected="scoreCallback" ></single-select>




        <!-- 输出log -->

      <div class="btns" style="margin-top:5%;">
        <button id="save-btn" type="submit" v-on:click.stop="btnSave()" style="bottom : 100%; width: 20%; height: 30%; ">保存</button>
        <button id="submit-btn" type="submit" v-on:click.stop="btnSub()" style="bottom : 100%; width: 20%; height: 30%; ">发布</button>
      </div>
  </div>
    </div>

  </div>

</template>

<script>
  // let originOptions = []
  // let selected=[]
  // let selectedList=[]
  import global from './global.vue'
  var hasNotLogin = [1]
  export default {
    name:"test",
    data() {
      return {
        circleUrl: global.photo,
        hasNotLogin:hasNotLogin,
        myId:global.userId,
        myName:global.name,
        questionId : null,
        single: {
          originOptions: [],
          selected: {}
        },
        labels: [],
        scores: {
          scoreOptions: [
            {id:0, name:'0'},
            {id:1, name:'10'},
            {id:2, name:'20'},
            {id:3, name:'30'},
            {id:4, name:'40'},
            {id:5, name:'50'},
          ],
          selected: {}
      },

        toolbar: true,
        title: "",
        content: {
          value:''
        },
        updatedContent:'',
        score: 0,

      }
    },

     created()
     {
       if(global.userId==-1){
         console.log(hasNotLogin)
       }
       else{
         hasNotLogin.splice(0,hasNotLogin.length)
         hasNotLogin.push(0)
       }
       var that = this
        // this.questionId = this.$route.query.questionId?this.$route.query.questionIdelse:-1;
       this.questionId =-1;

        // 获取问题类别列表
       this.$axios(
         {
           method:'get',
           url:global.host+'/admin',
           params:{
             action:"select",
             entity:"QuestionType",
           }
         }).then(response => {
           console.log(response)
         for (let i = 0; i < response.data.length; i++) {
                  this.single.originOptions.push(response.data[i]);
         }

         // 获取具体问题的信息
         if(this.questionId !== -1){
           this.$axios(
             {
               method:'post',
               url:global.host+'/submitQuestion',
               params:{
                 action:"select",
                 entity:"Question",
                 questionId:this.questionId
               }
             }).then(response => {
             console.log(response)

             response = response.data
             // console.log(response[0].type)
             this.single.selected = this.single.originOptions.find(item =>{
               console.log(parseInt(item.id), parseInt(response[0].type))
               return parseInt(item.id) === parseInt(response[0].type)
             });


             this.title = response[0].title
             this.content.value = response[0].content
             response[1].map(
               (item) => {
                 // console.log(item)
                 that.labels.push(item.name)
               })
             this.scores.selected = this.scores.scoreOptions.find(
               item =>{
                 return parseInt(item.name) === parseInt(response[0].reward)
               }

             )
              this.updatedContent = this.content.value;

           })
         }
         
       })


       // this.$nextTick()
       // document.getElementById("content").value = this.content
     },

    updated(){
      console.log(this.labels)
      console.log(this.labels.reduce(
        (total, ele) =>{
          total += ele
          total += ' '
          return total
        }, ''))

    },

    methods: {
      singleCallback: function (data) {
        this.single.selected = data;
        console.log('父级元素调用singleCallback 选中的是' + JSON.stringify(data))
      },
      labelsCallback: function (data) {
        this.labels = data;
        console.log('父级元素调用customMultipleInputed 选中的是' + JSON.stringify(data))

      },
      scoreCallback: function (data) {
        this.scores.selected = data
      },
      changeContent: function (content) {
        this.updatedContent = content
      },
      btnSave: function () {
        this.$axios(
          {
            method: 'post',
            url: global.host+'/submitQuestion',
            params: {
              action: "update",
              entity: "Question",
              content: encodeURI(this.updatedContent),
              title: encodeURI(this.title),
              labels: encodeURI(this.labels.reduce(
                (total, ele) => {
                  total += ele
                  total += ' '
                  return total
                }, ''
              )),
              questionType: this.single.selected.id,
              questionId: this.questionId,
              isReleased: 0,
              score: this.scores.selected.name
            }
          }).then(response => {
          this.$router.push({
            path: '/',
            query: {
            }
          });
        })
      },
      btnSub: function () {
        this.$axios(
          {
            method: 'post',
            url: global.host+'/submitQuestion',
            params: {
              action: "update",
              entity: "Question",
              content: encodeURI(this.updatedContent),
              title: encodeURI(this.title),
              labels: encodeURI(this.labels.reduce(
                (total, ele) => {
                  total += ele
                  total += ' '
                  return total
                }, ''
              )),
              questionType: this.single.selected.id,
              questionId: this.questionId,
              isReleased: 1,
              score: this.scores.selected.name
            }
          }).then(response => {
          this.$router.push({
            path: '/',
            query: {
            }
          });
        })
      },
      toLogin:function(){
        this.$confirm('是否登陆?', '提示', {
          confirmButtonText: '前往登陆',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
          this.$router.push({
            path: '/Login',
            query: {
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消登陆'
          });
        });
      },
      toMyHome:function () {
        this.$router.push({
          path: '/MyHome',
          query: {
          }
        })
      },
      toHome:function(){
        this.$router.push({
          path: '/',
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
    }

  }
</script>>


<style scoped>
  @import "../assets/icon/iconfont.css";
  @import "../components/css/guide.css";
  @import "css/searchBar.css";
  @import "../components/css/buttonBox.css";
  @import "http://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css";

 h4{margin: 10px;padding: 0px;font-family: "Microsoft Yahei";}
/*  *, *:before, *:after {
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
  }*/

  #layout {
    text-align: left;
  }
  #layout >h4{
    margin: 10px;padding: 0px;font-family: "Microsoft Yahei";
  }
  #layout > header, .btns {
    /*padding: 40px 0;*/
    padding-top: 40px;
    padding-bottom: 5px;
    width: 90%;
    margin: 0 auto;
  }
  .wrapper{
    width: 20%;
    /*display: inline;*/
    /*margin: 20px 0px 0px 20px;*/
  }
  .btns {
    padding-top: 0;
    margin: 0 0px;
  }
  .btns button {
    padding: 2px 0px;
  }
  .btns button, .btn {
    padding: 8px 10px;
    background: #fff;
    border: 1px solid #ddd;
    -webkit-border-radius: 3px;
    border-radius: 3px;
    cursor: pointer;
    -webkit-transition: background 300ms ease-out;
    transition: background 300ms ease-out;
  }
  .btns button:hover, .btn:hover {
    background: #f6f6f6;
    border-color: rgba(189,81,81,.8);
  }
  .mb-3{
    margin-bottom: 1rem!important;
    width: 100%;
  }
  .btns button:focus{
    outline: none;
  }
  #triggerBtn{
    background:none;
    border: none;
    float: right;

  }
  .input-group .form-control{
    height: 100px;
    width:100%;
    box-shadow: 0 0 8px rgba(181, 185, 189, 0.6);
    padding-right:20%;
  }
  .form-control:focus{
    /*box-shadow: none;*/
    /*border: 2px solid salmon;*/
    border-color: rgba(189,81,81,.8);
    box-shadow: 0 0 8px rgba(189,81,81,.6);
  }

  .photo{
    /*float: right;*/
    left: 30%;
    position: relative;

    /*right: ;*/
  }
  .QuestionContent{
    position: absolute;
    width: 64%;
    margin-left: 11%;
    display: inline;
    top:12%
  }
  .input{
    width: 100%;
    height: 600px;
  }
  .options{
    position: absolute;
    left: 76%;
    width: 401px;
    color: black;
    background-color: rgba(246,246,246,1);
    padding-left: 50px;
    top:10%;
  }
  #title{
    font-size: 25px;
    width: 79%;
    height: 50px;
  }

</style>
