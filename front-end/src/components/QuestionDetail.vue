<template>
  <div>
    <div class="top">
      <ul class="nav" style="padding-left: 9%;">
        <li class="link01">:D&nbsp&nbsp答</li>
        <li class="nav-item">
          <a class="nav-link" @click="toHome"><i class="iconfont">&#xe625;</i>&nbsp&nbsp主页</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" @click="toQuestion" id="link03"><i class="iconfont">&#xe7bf;</i>&nbsp&nbsp问题</a>
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
    <div class="titleBox">
      <span class="title">{{QuestionDetail[0].title}}</span>
      <div class="labelBox">
          <span v-for="label in QuestionDetail[0].labels">
             #{{label.name}}&ensp;
          </span>
      </div>
    </div>
    <div class="content">
      <div class="contentBox" id = "test-editormd-view">
        <div  v-html="questionContent" id="question-content"></div>
        <input-fielda :content="{value:QuestionDetail[0].content}" :previewing="true" :hide="true" @loaded="setHtml"></input-fielda>
        <!--                 <span style="white-space: pre-line">{{QuestionDetail[0].content}}</span><br>-->
        <div class="bottomLine">
          <div style="color:rgba(145,139,139,1);font-size:20px;display:inline-block;vertical-align:top;">{{QuestionDetail[0].date}}</div>
          <div style="font-size:20px;display:inline-block;width:150px;margin-left:50px;vertical-align:top;color:gold;">
            <i class='iconfont 'style="font-size:20px;">&#xe61a;</i>积分悬赏:{{QuestionDetail[0].reward}}
          </div>
          <div style="color:blue;font-size:20px;margin-left:300px;cursor:pointer;display:inline-block;vertical-align:top;" @click="dialogVisible = true">我要回答</div>

        </div>
      </div>
      <!--问题展示end-->

      <div class="question-answer">
        <div class="answer-header">
          <span>这些人回答了</span>
        </div>
        <div v-for="answer,index in answerList" class="answerDiv">
          <el-avatar style="display:inline-block;width:70px;height:70px;fit:fill;vertical-align: top" size="large" :src="circleUrl"></el-avatar>
          <div style="display:inline-block;width:620px;">
            <div class="answerInfo">
              <el-tooltip content="点击查看用户信息" placement="bottom" effect="light">
                <p style="cursor: pointer;width:150px;" >{{answer.answerer}}</p>
              </el-tooltip>
              <p style="color:gray;width:200px;">{{answer.dateTime}}</p>
              <p v-if="!answer.zan" style="margin-left:75px;cursor: pointer;width:100px;" :data-item="index" @click="toAward">我要赞赏</p>
              <p v-else style="margin-left:75px;cursor: pointer;width:100px;" >已赞赏</p>
              <p style="margin-left:10px;width:60px;">{{index}}楼</p>
            </div>
            <span class="answerContent">
                  {{answer.content}}
            </span>

            <div style="display:block;margin-top:20px;font-size:18px;">
              <span v-if="!answer.commentShow" style="cursor:pointer;color:#bd5151" @click="showCommentOf" :data-item="index">点击查看评论>></span>
              <span v-else style="cursor:pointer;color:#bd5151" @click="showCommentOf" :data-item="index">点击收起评论</span>
              <span v-if="!answer.inputShow" style="cursor:pointer;margin-left:50px;color:skyblue;" :data-item="index" @click="toComment" >我要评论</span>
              <div v-else style="cursor:pointer;margin-left:50px;color:orangered;"  >
                <span :data-item="index" @click="uploadComment">提交</span>
                <span :data-item="index" @click="cancelComment">取消</span>
              </div>
              <span v-if="!answer.like"  style="cursor:pointer;margin-left:50px;color:skyblue;" :data-item="index" @click="giveLike" > <i class='iconfont 'style="font-size:18px;">&#xe65c;</i>点赞({{answer.numOfAgree}})</span>
              <span v-else  style="cursor:pointer;margin-left:50px;color:red;" :data-item="index" @click="giveLike" > <i class='iconfont 'style="font-size:18px;">&#xe65c;</i>已赞({{answer.numOfAgree}})</span>
            </div>
            <el-input v-if="answer.inputShow"
                      type="textarea"
                      :autosize="{ minRows: 2, maxRows: 4}"
                      placeholder="请输入内容"
                      v-model="answer.commentContent" >
            </el-input>

            <div class="showComments" v-if="answer.commentShow">
              <li v-for="comment in commentList[index]" class="commentList" style="word-break: break-word">
                <span style="color:blue;cursor:pointer">{{comment.user}}</span><span>:{{comment.content}}</span>
              </li>
            </div>
          </div>
        </div>
      </div>
      <!--回复展示end-->
    </div>
    <div id="disRight">
      <!--卡片1-->
      <div class="cright">
        <h5 class="card-title">Q/A START</h5>
        <div class="btns" style="margin-top:0;">
          <button @click="dialogVisible=true"><i class="iconfont">&#xe628;</i>写回答</button>
          <button @click="toQuestionSubmit"><i class="iconfont">&#xe703;</i>写问题</button>
        </div>
      </div>
      <!--卡片2-->
      <div class="cright" v-show="!hasNotLogin[0]">
        <button class="rightBtn"><a ><i class="font2 iconfont">&#xe613;</i>&nbsp&nbsp我的收藏</a></button><br><br>
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

    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose" >
      <span>请写下评论(右侧为效果预览)</span>
      <div  style="margin-top:20px;">
<!--        <input-fielda :content="newAnswer" :hide="false" :previewing="false"></input-fielda>-->
        <inputfield ref="input" class="inputfield"></inputfield>
        <span slot="footer" class="dialog-footer">
           <el-button @click="dialogVisible = false">取 消</el-button>
           <el-button @click="upload">提交</el-button>
        </span>
      </div>
    </el-dialog>


  </div>

</template>

<script>
  import global from './global.vue'
  import util from '@/util.js'
  import Input from './InputField.vue'
  /*var data =
    {title:"这该怎么办",
      content:"情况是这样的，我和女朋友在一起快3年了，就是那种大学到工作的校园恋情。所以我的确是很珍惜这段感情。\n" +
        "篮球，是我从初中到大学唯一一个没有改变过的爱好。喜欢独行侠也很长时间，最喜欢的球员是077，也一直很想去美国看一次他们的现场。工作了1年多，自己也攒了一点钱，去美国来回的机票钱反正是够了。",
      Questioner:"wlzdd",
      date:"2019-6-18",
      labels:["王者荣耀","游戏"]}
  var PeInfo={img:"../assets/image/test.jpg",Questioner:"wlzdd",numOfQuestion:5,numOfAnswer:5,points:5}
  var answerList=[{img:"../assets/image/test.jpg",answerer:"wlzdd",dateTime:"2019-6-18 7:54",content:"撒的都",answerId:5},
                  {img:"../assets/image/test.jpg",answerer:"wlzdd",dateTime:"2019-6-18 7:54",content:"撒的是大家把第三比第三季度那是你你删的暖单独insad那第四年迪士尼那第四那第四厺都",answerId:5},
    {img:"../assets/image/test.jpg",answerer:"wlzdd",dateTime:"2019-6-18 7:54",content:"撒的都",answerId:5},
    {img:"../assets/image/test.jpg",answerer:"wlzdd",dateTime:"2019-6-18 7:54",content:"撒的都",answerId:5},
    {img:"../assets/image/test.jpg",answerer:"wlzdd",dateTime:"2019-6-18 7:54",content:"撒的都",answerId:5},
    {img:"../assets/image/test.jpg",answerer:"wlzdd",dateTime:"2019-6-18 7:54",content:"撒的都",answerId:5},
    {img:"../assets/image/test.jpg",answerer:"wlzdd",dateTime:"2019-6-18 7:54",content:"撒的都",answerId:5},]
  var commitList=[{user:"asd",commit:"dasd sad dsa sad s "},
    {user:"asd",commit:"dasd sad dsa sad s "},
    {user:"asd",commit:"dasd sad dsa sad s "}
    ]*/
  var QuestionDetail =[]
  var PeInfo=[]
  var answerList=[]
  var commentList=[]
  var isAnswer={key:false}
  var dialogVisible=false
  var userId=""
  var hasAwarded=0
  var hasNotLogin = [1]
  export default {
    name: "QuestionDetail",
    components: {"inputfield": Input},
    data() {
      return {
        QuestionDetail: QuestionDetail,
        peInfo: PeInfo,
        answerList: answerList,
        commentList: commentList,
        isAnswer: isAnswer,
        textarea2:"",
        dialogVisible:dialogVisible,
        hasAwarded:hasAwarded,
        circleUrl: global.photo,
        hasNotLogin:hasNotLogin,
        myId:global.userId,
        myName:global.name,
        preview:null,
        questionContent:'',
        newAnswer: {value:null}
      }
    },
    created() {
      // that = this
      if(global.userId==-1){
        console.log(hasNotLogin)
      }
      else{
        hasNotLogin.splice(0,hasNotLogin.length)
        hasNotLogin.push(0)
      }
      console.log("-----接受数据-------")
      userId=this.$route.query.userId
      this.$axios.get(global.host + '/detail',
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          params: {
            action: "select",
            entity: "Question",
            questionId: this.$route.query.questionId
          }
        })
        .then(function (response) {
          console.log(response)
          var l = {
            title: response.data.title,
            content: response.data.content,
            Questioner: response.data.user,
            date: util.formatDate(response.data.date),
            labels: JSON.parse(response.data.label),
            reward:response.data.reward,
            userId:userId
          }
          QuestionDetail.push(l)

          console.log("-----以下是问题详细-----")
          console.log(QuestionDetail)
        });
      this.$axios.get(global.host + '/detail',
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          params: {
            action: "select",
            entity: "Answer",
            questionId: this.$route.query.questionId,
            userId:global.userId,
            index: 0,
            num: 50
          }
        })
        .then(function (response) {
          console.log("======answerList")
          console.log(response)
          for (var i = 0; i < response.data.length; i++) {
            var l = {
              img: response.data[i].photo,
              answerer: response.data[i].name,
              dateTime: util.formatTime(response.data[i].time),
              content: response.data[i].content,
              answerId: response.data[i].id,
              commentShow: false,
              inputShow:false,
              commentContent:"",
              hasBeenAwarded:0,
              like:response.data[i].hasAgreed,
              numOfAgree:response.data[i].numOfAgree,
              zan:response.data[i].givenScore
            }
            answerList.push(l)
            commentList.push([])
          }
        })
      this.$axios(
        {
          method: 'post',
          url: global.host + '/G-A',
          params: {
            questionId: this.$route.query.questionId,
            email:global.email,
          }
        }).then(res => {
        console.log("----以下是是否悬赏信息--------")
        console.log(res)
        hasAwarded=res.data;
      });
    },
    destroyed(){
      QuestionDetail =[]
      PeInfo=[]
      answerList=[]
      commentList=[]
      isAnswer={key:false}
      var hasNotLogin = [1]
    },
    methods: {
      showCommentOf: function (e) {
        var i = e.target.getAttribute('data-item')
        console.log(e.target.getAttribute('data-item'))
        if (answerList[i].commentShow) {
          answerList[i].commentShow = false
          commentList[i].splice(0, commentList[i].length)
        } else {
          commentList[i].splice(0, commentList[i].length)
          answerList[i].commentShow = true
          this.$axios.get(global.host + '/detail',
            {
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              params: {
                action: "select",
                entity: "Comment",
                answerId: answerList[i].answerId,
                index: 0,
                num: 20
              }
            })
            .then(function (response) {
              console.log(response)
              for (var u = 0; u < response.data.length; u++) {
                var l = {
                  user: response.data[u].name,
                  content: response.data[u].content
                }
                commentList[i].push(l)
              }
              console.log("----------" + i)
              console.log(commentList[i])
            });
        }
      },
      toComment: function (e) {
        var i = e.target.getAttribute('data-item')
        answerList[i].inputShow = true
      },
      toAnswer: function (e) {
        isAnswer.key = true
        dialogVisible = true
        console.log(isAnswer + dialogVisible)
      },
      toQuestionSubmit(){
          this.$router.push({
            path: '/QuestionSubmit',
            query: {
            }
          })
      },
      upload: function (e) {
        console.log(this.$refs.input)
        var i = document.getElementById("content").value
        console.log(document.getElementById("content").value)
        this.$confirm('确认提交?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios({
            method: 'post',
            url: global.host + '/detail',
            params: {
              action: "insert",
              entity: "Answer",
              questionId: this.$route.query.questionId,
              mail: global.email,
              content: encodeURI(i)
            }
          })
            .then(function (response) {
              console.log(response)
              location.reload()
            })
        });
      },
      uploadComment: function (e) {
        var u = e.target.getAttribute('data-item')
        console.log(answerList[u].commentContent)
        this.$confirm('确认提交评论?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios({
            method: 'post',
            url: global.host + '/detail',
            params: {
              action: "insert",
              entity: "Comment",
              answerId: answerList[u].answerId,
              mail: global.email,
              content: encodeURI(answerList[u].commentContent)
            }
          })
            .then(function (response) {
              answerList[u].commentContent = ''
              answerList[u].inputShow = false
              location.reload()
            });
        });
      },
      cancelComment: function (e) {
        var i = e.target.getAttribute('data-item')
        answerList[i].inputShow = false
        answerList[i].commentContent = ''
      },
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {
          });
      },
      giveLike: function (e) {
        var u = e.target.getAttribute('data-item')
        console.log(global.userId + "给" + answerList[u].answerId + "点了赞")
        if(answerList[u].like==1){}
        else {
          answerList[u].like = 1
          answerList[u].numOfAgree += 1
          this.$axios({
            method: 'post',
            url: global.host + '/ScoreSystem',
            params: {
              answerId: answerList[u].answerId,
              email: global.email,
            }
          })
            .then(function (response) {
              console.log("------点赞反馈-------")
              console.log(response)
            });
        }
      },
      toAward: function (e) {
        var u = e.target.getAttribute('data-item')
        console.log("用户ID："+global.userId )
        console.log("提问者ID："+QuestionDetail[0].userId)
        console.log("回答ID："+answerList[u].answerId)
        if (global.userId != QuestionDetail[0].userId)
        {
          this.$alert('只有提问者才可以赞赏', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
        }
        else if(hasAwarded)
        {
          this.$alert('您的悬赏已经使用', '提示', {
            confirmButtonText: '确定',
            callback: action => {
            }
          });
        }
        else
        {
          this.$axios({
            method: 'get',
            url: global.host + '/ScoreSystem',
            params: {
              answerId: answerList[u].answerId,
              questionId: this.$route.query.questionId,
            }
          })
            .then(function (response) {
              answerList[u].zan=1
              hasAwarded=1
            });
          this.$confirm("确认赞赏该回答？", '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {

          })
        }
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
      setHtml:function (html) {
        this.questionContent = html
      }

    }
  }
</script>

<style scoped>
  @import "../assets/style/icon/iconfont.css";
  @import "../assets/icon/iconfont.css";
  @import "../components/css/guide.css";
  @import "css/searchBar.css";
  @import "../components/css/buttonBox.css";
  @import "http://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css";
  #question-content >>> img{
    max-width: 100%;
  }
  .top{
    position:fixed;
    border-bottom: 1px solid #bd5151;
  }
  .titleBox{
    width:100%;
    height:150px;
    background-color: white;
    margin-top:70px;
    vertical-align: middle;
    box-shadow: 0 0 8px rgba(181, 185, 189, 0.6);
  }
  .title{
    margin-left:380px;
    font-size:28px;
    display:block;
    padding-top:30px;
  }
  #disRight{
    left:62%;
    top:25%;
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
  a:hover{
    cursor: pointer;
  }
  .labelBox{
    margin-left:380px;
    display:inline-block;
    font-size:20px;
    margin-top:20px;
    color:rgba(11,51,232,1);
  }
  .content{
    display:inline-block;
    margin-left: 350px;
    width:750px;
    height:auto;
    font-size:25px;
    line-height:40px;
    vertical-align: top;
  }
  .contentBox{
    display:block;
    background-color: white;
    margin-top:20px;
    width:700px;
    height:auto;
    font-size:20px;
    padding:30px 30px 20px 30px;
    line-height:40px;
    vertical-align: top;
    box-shadow: 0 0 8px rgba(181, 185, 189, 0.6);
  }
  .peInfo{
    display:inline-block;
    background-color: white;
    width:500px;
    height:500px;
    margin-left:50px;
    vertical-align: top;
    margin-top:20px;
    position:fixed;
  }
  .bottomLine{
    margin-top:50px;
  }
  .question-answer{
    margin-top:30px;
    display:block;
    background-color: white;
    border:1px solid rgba(51,143,211,1);
    width:760px;
    height:auto;
    padding-bottom:50px;
  }
  .answer-header{
    height:50px;
    border-bottom:1px solid rgba(187,187,187,1) ;
    margin-left:30px;
    margin-right: 30px;
    color:#1C56E3 ;
    font-size:24px;
    vertical-align: middle;
    padding-top:20px;
  }
  .answerDiv{
    margin-top:20px;
    margin-left:30px;
    margin-right:30px;
    padding-bottom: 30px;
    border-bottom: 1px solid rgba(187,187,187,1);
  }
  .answerInfo answerContent{
    display:block;
    margin:20px;
  }
  .answerInfo{
    font-size:18px;
    border-bottom: 1px dashed rgba(187,187,187,1);
  }
  .answerContent{
    margin-top:20px;
    margin-bottom:20px;
    font-size:20px;
  }
  p{
    display:inline-block;
    margin-top:0;
    margin-bottom:0;
  }
  .commentList{
    list-style: none;
    text-decoration: none;
  }
  .showComments{
    background-color: rgba(246,246,246,0.5);
    font-size:20px;
    height:auto;
    width:600px;
    padding-bottom:30px;
  }

  .inputfield{
    width:800px;
    height:500px;
    z-index: 1000;
    postition:fixed;
    top:200px;
  }
</style>
