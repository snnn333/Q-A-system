<template>
  <div id="body">
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
    <div class="top">
      <ul class="nav" style="padding-left: 9%;">
        <li class="link01">:D&nbsp&nbsp答</li>
        <li class="nav-item">
          <a class="nav-link" @click="toHome"><i class="iconfont">&#xe625;</i>&nbsp&nbsp主页</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="#" id="link03"><i class="iconfont">&#xe7bf;</i>&nbsp&nbsp问题</a>
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
            <button id="triggerBtn" @click="dialogVisible = true"><li><a href="#" data-toggle="tooltip" data-placement="bottom" title="登录"><i class="iconfont">&#xe601;</i></a></li></button>
          </div>
          <div v-else>
            <button id="personBtn">
              <div @click="toMyHome">
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
    <!--导航栏end-->

    <div id="classList" class="listContainer" >
      <div style="font-size:30px;text-align: center;width:100%">问题索引</div>
      <div class="indexSearch">
       <input
        placeholder="请输入关键字"
        v-model="inputSearch"
        style="width:60%;margin-left:45px;border:2px solid #c5464a;position:absolute;margin-top:11px;" @keyup.enter="indexSearch">
        <button @click="indexSearch">搜索</button>
      </div>
      <div class="classBlock">
        <div class="block-name">类别</div>
        <ul class="block-item-wrapper">
          <li v-for="item,index in classList" :class="{'block-item' :1,'on':clickClass[index]==1}" :data-itemId="index" @click="choseClass">{{item.name}}</li>
        </ul>
      </div>
      <div id="labelBlock" class="labelBlock">
        <div class="block-name">标签</div>
        <ul class="block-item-wrapper">
          <li v-for="item,index in labelList" :class="{'block-item' :1,'on':clickLabel[index]==1}" :data-itemId="index" @click="choseLabel" >#{{item}}</li>
        </ul>
      </div>


    </div>
    <!--分类栏 end-->
    <div id ="mainContent" class="questionBox">
      <p class="show-headline" >
        <span class="t1">主题</span>
        <span class="t2">提问者</span>
        <span class="t3">回复/浏览</span>
      </p>
      <!--标题栏end-->
      <li v-for="question,index in questionList" class="question">
        <div class="titleBox">
          <span @click="toQuestionDetail" :data-item="index" class="toQuestion" >{{question.title}}</span>
        </div>
        <div class="authorBox">
          <span class="alink"  >{{question.Questioner}}</span>
          <br>
          <span style="color:#808080;cursor: initial;height: 20px;font-size:18px;">{{question.date}}</span>
        </div>
        <span  class="ansourBox" style="font-size: 20px;">{{question.answer}}&nbsp;/&nbsp;{{question.view}}</span>
      </li>
    </div>
  </div>
  <!--问题栏end-->

</template>

<script>
  import global from './global.vue'
  import util from '@/util.js'
  var that=this
  var message=""
  var questionList = []
  var classList=[{name:"全部"}]
  var clickClass=[1]
  var clickLabel=[1]
  var labelList=["全部"]
  var chosenOne=0;
  var inputSearch=""
  var hasNotLogin = [1]
  export default {
    name: 'QuestionShow',
    data()
    {
      return{
        message: '',
        classList:classList,
        questionList:questionList,
        inputSearch:inputSearch,
        labelList:labelList,
        clickClass:clickClass,
        clickLabel:clickLabel,
        circleUrl: global.photo,
        hasNotLogin:hasNotLogin,
        myId:global.userId,
        myName:global.name,
        dialogVisible:false
      }
    },
    watch:{
       inputSearch(newName, oldName){
         console.log("inputSearch: "+newName);
         inputSearch=newName
       }
    },
    created() {
      if(global.userId==-1){
        console.log(hasNotLogin)
      }
      else{
        hasNotLogin.splice(0,hasNotLogin.length)
        hasNotLogin.push(0)
      }
      console.log("show1created在执行")
      console.log("-----------" + this.$route.query.lastClass + "------------")
      this.$axios.get(global.host + '/admin',
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          params: {
            action: "select",
            entity: "Label",
          }
        })
        .then(function (response) {
          for (var i = 0; i < response.data.length; i++) {
            labelList.push(response.data[i].name)
            clickLabel.push(0)
          }
          console.log(labelList)
        });
      this.$axios(
        {
          method: 'get',
          url: global.host + '/main',
          params: {
            action: "select",
            entity: "QuestionType"
          }
        }).then(res => {
          console.log(res)
        for (var i = 0; i < res.data.length; i++) {
          var l = {
            icon: res.data[i].icon,
            name: res.data[i].name
          }
          classList.push(l)
          clickClass.push(0)
        }
        /*if (this.$route.query.lastClass) {
          for (var i = 0; i < classList.length; i++) {
            console.log(this.$route.query.lastClass.trim() + "   " + classList[i].name)
            if (this.$route.query.lastClass.trim() == classList[i].name.trim()) {
              chosenOne = i
              break
            }
          }
        }
        console.log(console.log("---" + chosenOne + "---"))
        console.log("questionList1初始化")
        if (chosenOne == 0) {
          this.$axios(
            {
              method: 'get',
              url: global.host + '/test/main',
              params: {
                action: "select",
                entity: "Question",
                QuestionType: encodeURI(classList[chosenOne].name),
                index: 0,
                num: 5,
                orderBy: "time"
              }
            }).then(res => {
            console.log(res)
            for (var i = 0; i < res.data.length; i++) {
              var l = {
                title: res.data[i].title,
                Questioner: res.data[i].name,
                date: util.formatDate(res.data[i].time),
                view: res.data[i].frequency,
                answer: res.data[i].numOfAnswer,
                questionId:res.data[i].id,
                userId:res.data[i].userId
              }
              questionList.push(l)
            }
          })
        } else {
          this.$axios(
            {
              method: 'get',
              url: global.host + '/test/main',
              params: {
                action: "select",
                entity: "Question",
                QuestionType: encodeURI(classList[chosenOne].name),
                index: 0,
                num: 20,
                orderBy: "time"
              }
            }).then(res => {
              console.log(res)
            for (var i = 0; i < res.data.length; i++) {
              var l = {
                title: res.data[i].title,
                Questioner: res.data[i].name,
                date: util.formatDate(res.data[i].time),
                view: res.data[i].frequency,
                answer: res.data[i].numOfAnswer,
                questionId:res.data[i].id,
                userId:res.data[i].userId
              }
              questionList.push(l)
            }
          })
        }*/
        this.$axios(
          {
            method: 'get',
            url: global.host + '/index',
            params: {
              action: "select",
              entity: "Question",
              keywords:"",
              questionType: encodeURI("全部"),
              labels:encodeURI("全部"),
              index: 0,
              num: 50,

            }
          }).then(res => {
            console.log("******************")
          console.log(res)
          for (var i = 0; i < res.data.length; i++) {
            var l = {
              title: res.data[i].title,
              Questioner: res.data[i].name,
              date: util.formatDate(res.data[i].time),
              view: res.data[i].frequency,
              answer: res.data[i].numOfAnswer,
              questionId:res.data[i].id,
              userId:res.data[i].userId
            }
            questionList.push(l)
          }

        })
      })
    },
    /* mounted(){
       console.log("show2mounted在执行")
       console.log("555"+chosenOne)

     },*/
  destroyed(){
    console.log("QuestionShow has been destroyed")
    questionList.splice(0,questionList.length)
    classList.splice(0,classList.length)
    classList.push({name:"全部"})
    clickClass.splice(0,clickClass.length)
    clickClass.push(1)
    clickLabel.splice(0,clickLabel.length)
    clickLabel.push(1)
    labelList.splice(0,labelList.length)
    labelList.push("全部")
    var hasNotLogin = [1]
  },
    methods: {
      toQuestionDetail:function (e){
        if(global.userId==-1){
          this.$confirm('查看问题详情需要您登陆, 是否继续?', '提示', {
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
        }
        else {
          var i = e.target.getAttribute('data-item')
          console.log(e.target.getAttribute('data-item'))
          this.$router.push({
            path: '/QuestionDetail',
            query: {
              questionId: questionList[i].questionId,
              userId: questionList[i].userId
            }
          })
        }
      },
      choseClass:function(e){
        var u=e.target.getAttribute('data-itemId')
        if(u==0&&clickClass[u]==1){}
        else if(u==0&&clickClass[u]==0){
          for(var i=0;i<clickClass.length;i++){
            clickClass[i]=0
          }
          clickClass.splice(u,1,1)
        }
        else if(clickClass[u]==1){
          clickClass.splice(u,1,0)
        }
        else{
          clickClass.splice(u,1,1)
          clickClass.splice(0,1,0)
        }
        var classLimit=""
        var labelLimit=""
        for(var i=0;i<clickClass.length;i++){
          if(clickClass[i]==1){
            classLimit+=classList[i].name+" "
          }
        }
        for(var i=0;i<clickLabel.length;i++){
          if(clickLabel[i]==1){
            labelLimit+=labelList[i]
          }
        }
        console.log(classLimit)
        console.log(labelLimit)
        this.$axios(
          {
            method: 'get',
            url: global.host + '/index',
            params: {
              action: "select",
              entity: "Question",
              keywords:encodeURI(inputSearch),
              questionType: encodeURI(classLimit),
              labels:encodeURI(labelLimit),
              index: 0,
              num: 20,
            }
          }).then(res => {
          console.log(res)
          questionList.splice(0,questionList.length)
          for (var i = 0; i < res.data.length; i++) {
            var l = {
              title: res.data[i].title,
              Questioner: res.data[i].name,
              date: util.formatDate(res.data[i].time),
              view: res.data[i].frequency,
              answer: res.data[i].numOfAnswer,
              questionId: res.data[i].id,
              userId: res.data[i].userId
            }
            questionList.push(l)
          }
        });


      },
      choseLabel:function(e){
        var u=e.target.getAttribute('data-itemId')
        if(u==0&&clickLabel[u]==1){}
        else if(u==0&&clickLabel[u]==0){
          for(var i=0;i<clickLabel.length;i++){
            clickLabel[i]=0
          }
          clickLabel.splice(u,1,1)
        }
        else if(clickLabel[u]==1){
          clickLabel.splice(u,1,0)
        }
        else{
          clickLabel.splice(u,1,1)
          clickLabel.splice(0,1,0)
        }
        var classLimit=""
        var labelLimit=""
        for(var i=0;i<clickClass.length;i++){
          if(clickClass[i]==1){
            classLimit+=classList[i].name+" "
          }
        }
        for(var i=0;i<clickLabel.length;i++){
          if(clickLabel[i]==1){
            labelLimit+=labelList[i]
          }
        }
        this.$axios(
          {
            method: 'get',
            url: global.host + '/index',
            params: {
              action: "select",
              entity: "Question",
              keywords:encodeURI(inputSearch),
              questionType: encodeURI(classLimit),
              labels:encodeURI(labelLimit),
              index: 0,
              num: 20,
            }
          }).then(res => {
          console.log(res)
          questionList.splice(0,questionList.length)
          for (var i = 0; i < res.data.length; i++) {
            var l = {
              title: res.data[i].title,
              Questioner: res.data[i].name,
              date: util.formatDate(res.data[i].time),
              view: res.data[i].frequency,
              answer: res.data[i].numOfAnswer,
              questionId: res.data[i].id,
              userId: res.data[i].userId
            }
            questionList.push(l)
          }
        });
      },
      toWrite:function(e){
       this.$router.push({path:"./QuestionSubmit.html"})
      },
      toPersonalHome(){
        this.$router.push({path:"/changeInfo"})
      },
      indexSearch:function(e){
        console.log(inputSearch)
        var classLimit=""
        var labelLimit=""
        for(var i=0;i<clickClass.length;i++){
          if(clickClass[i]==1){
            classLimit+=classList[i].name+" "
          }
        }
        for(var i=0;i<clickLabel.length;i++){
          if(clickLabel[i]==1){
            labelLimit+=labelList[i]
          }
        }
        console.log(classLimit)
        console.log(labelLimit)
        this.$axios(
          {
            method: 'get',
            url: global.host + '/index',
            params: {
              action: "select",
              entity: "Question",
              keywords:encodeURI(inputSearch),
              questionType: encodeURI(classLimit),
              labels:encodeURI(labelLimit),
              index: 0,
              num: 20,
            }
          }).then(res => {
          questionList.splice(0,questionList.length)
          for (var i = 0; i < res.data.length; i++) {
            var l = {
              title: res.data[i].title,
              Questioner: res.data[i].name,
              date: util.formatDate(res.data[i].time),
              view: res.data[i].frequency,
              answer: res.data[i].numOfAnswer,
              questionId: res.data[i].id,
              userId: res.data[i].userId
            }
            questionList.push(l)
          }

        });
      },
      toLogin:function(){
        this.$router.push({
          path: '/Login',
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
      toMyHome:function () {
    this.$router.push({
      path: '/MyHome',
      query: {
      }
    })
  },
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      }
    }
  }
</script>

<style scoped>
  @import "../assets/icon/iconfont.css";
  @import "../components/css/guide.css";
  @import "css/searchBar.css";
  @import "../components/css/buttonBox.css";
  @import "http://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css";
  #body{
    width: 100%;
    height: 100%;

  }
  .listContainer{
    margin-top:100px;
    margin-left:50px;
    width:400px;
    height:auto;
    padding-bottom: 0px;
    background-color:white;
    display:inline-block;
    line-height: 60px;
    font-weight:500;
    border:1px solid #bd5151;
    position:fixed;
  }
  #chosen{
    margin: 0 auto;
    width:80%;
    display:block;
    font-family:Roboto;
    color:#bd5151;
    font-size:30px;
    text-decoration: none;
    border-bottom: 2px solid rgba(187,187,187,1);
  }
  .ui-link{
    margin: 0 auto;
    width:80%;
    display:block;
    font-size:20px;
    font-family:Roboto;
    color: #888;
    text-decoration: none;
    border-bottom: 2px solid rgba(187,187,187,1);
    color:rgba(0,0,0,0.87);

  }
  .ui-link:hover{
    background-color: #f1f1f1;
    z-index:100;
    box-shadow:#bd5151 0px 0px 10px;
    cursor:pointer;
  }

  .questionBox{
    width:60%;
    height:auto;
    margin-top:100px;
    margin-left:500px;
    background-color:white;
    display:inline-block;
    line-height: 50px;
    vertical-align:top;
  }

  .show-headline{
    width: 100%;
    height: 50px;
    line-height: 50px;
    background-color: #edf1f4;
    color: #555;
    border-bottom: 2px solid rgba(204,82,82,1);
    font-weight: 700;
    margin:0;

  }
  span{
    display:inline-block;
    margin:0;
  }
  p {
    display: block;
    -webkit-margin-before: 1em;
    -webkit-margin-after: 1em;
    -webkit-margin-start: 0px;
    -webkit-margin-end: 00px;
  }
  .t1 {
    width:60%;
    margin-left: 20px;
  }
  .t2{
    width: 20%;
  }
  .t3{
    width: 10%;
    text-align: center;
  }
  .titleBox{
    width:60%;
    height:auto;
    vertical-align: middle;
    line-height: 1.5;
    display:inline-block;
    margin-left: 20px;
    font-size: 20px;
  }
  .authorBox{
    display:inline-block;
    height:auto;
    width:20%;
    line-height: 1.5;
    vertical-align: middle;
  }
  .ansourBox{
    vertical-align: middle;
    text-align:center;
    width:10%;
  }
  .alink{
    color: #005eac;
    text-align: left;
    cursor: pointer;
    height: 20px;
    font-size:18px;
    text-decoration: none;
  }
  .question{
    list-style: none;
    border-bottom: 2px solid #ccc;
    line-height:100px;
  }
  .question:hover{
    background-color: #f1f1f1;
  }
  .arrow-right {
    width: 7px;
    height: 7px;
    border-top: 2px solid rgba(187,187,187,1);
    border-right: 2px solid rgba(187,187,187,1);
    transform: rotate(45deg);
    text-align: right;
    margin-right:5px;
  }
  alink:hover
  {
    background-color: #005eac;
    color:white;
    z-index:100;
    color:#bd5151;
    box-shadow:#bd5151 0px 0px 10px;
  }

  .toQuestion{
    text-decoration: none;
    color: #555;
    cursor:pointer;
  }
  .toQuestion:hover{
    text-decoration:underline;
  }
  .ui-link{
    margin: 0 auto;
    width:80%;
    display:block;
    font-size:20px;
    font-family:Roboto;
    color: #888;
    text-decoration: none;
    border-bottom: 2px solid rgba(187,187,187,1);
    color:rgba(0,0,0,0.87);
  }
  #classList button{
    background-color: #c5464a;
    color:white;
    display:inline-block;
    margin-top:0px;
    margin-left:300px;
    height:38px;
  }
  .classBlock{
    width:100%;
    margin-top:50px;
    padding-left:30px;
    padding-right:20px;
    position: relative;
    margin-bottom: 16px;
    overflow: hidden;
    display:block;
  }
  .block-name{
    display: inline-block;
    float: left;
    width: 52px;
    height: 30px;
    line-height: 1;
    font-size: 18px;
    color: #99a2aa;
    vertical-align:top ;
  }
.block-item-wrapper{
  display: inline-block;
  width: 310px;
  overflow: hidden;
  -webkit-transition: all .3s linear;
  -o-transition: all .3s linear;
  transition: all .3s linear;
  min-height: 30px;
  list-style: none;
  padding-left:0px;
  vertical-align:top ;
  margin-top:0;
}
  .block-item{
   display: inline-block;
   vertical-align: top;
   line-height: 1;
   text-align: left;
   width: auto;
    margin-left:15px;
   height: 30px;
   font-size: 18px;
   white-space: nowrap;
   overflow: hidden;
   -o-text-overflow: ellipsis;
   text-overflow: ellipsis;
   padding-right: 2px;
   cursor: pointer;
   outline: 0;
 }
  .labelBlock{
    width:100%;
    margin-top:10px;
    padding-left:30px;
    padding-right:20px;
    position: relative;
    margin-bottom: 16px;
    overflow: hidden;
    display:block;
  }
  .on{
    color:#00a1d6
  }


</style>
