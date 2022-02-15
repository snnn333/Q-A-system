<template>
  <div id="body">
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
          <div>
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
      <li v-for="item,index in leftList" :class="{'ui-link':1,'chosen':leftChosen[index]==1}" @click="chose" :data-item="index">
        {{item}}
      </li>
    </div>
    <!--分类栏 end-->
    <div id ="mainContent" class="contentBox">
      <table align="center" >
        <tr class="firstLine">
          <td>用户名</td>
          <td>邮箱</td>
          <td>是否允许登录</td>
          <td>是否允许发言</td>
          <td>删除与提交</td>
        </tr>
        <tr v-for="item,index in userList" >
          <td>
            {{item.userName}}
          </td>
          <td>
            {{item.mail}}
          </td>
          <td>
            <el-select v-if="item.allowAdmin"  @change='getAdminValue' :data-itemId="index" v-model="item.allowAdmin2"
                       placeholder="是">
              <el-option
                v-for="item2 in options"
                :key="item2.value"
                :label="item2.label"
                :value="item2.value">
              </el-option>

            </el-select>
            <el-select v-else    @change='getAdminValue' :data-itemId="index" v-model="item.allowAdmin2"
                       placeholder="否">
              <el-option
                v-for="item2 in options"
                :key="item2.value"
                :label="item2.label"
                :value="item2.value">
              </el-option>
            </el-select>
          </td>
          <td>
            <el-select v-if="item.allowCommit" @change='getCommitValue'   size="2" v-model="item.allowCommit2">
              <el-option
                v-for="item2 in options"
                :key="item2.value"
                :label="item2.label"
                :value="item2.value">
              </el-option>

            </el-select>
            <el-select v-else  @change='getCommitValue' :data-itemId="index" v-model="item.allowCommit2">
              <el-option
                v-for="item2 in options"
                :key="item2.value"
                :label="item2.label"
                :value="item2.value">
              </el-option>
            </el-select>
          </td>
          <td>
            <span :data-itemId="index" class="click" @click="deleteUser" style="color:red">删除</span>
            <span :data-itemId="index" class="click" @click="confirm">提交更改</span>
          </td>
        </tr>

      </table>
      <el-pagination
        @current-change="handleCurrentChange"
        :page-size="9"
        layout="prev, pager, next, jumper"
        :total="whole">
      </el-pagination>
      </div>
      <!--内容显示栏end-->
  </div>


</template>

<script>
  import global from './global.vue'
  var that=this
  var message="";
  var isAllowAdmin=""
  var data = [
  ];
  var leftList=["用户管理","类别管理","标签管理"];
  var leftChosen=[1,0,0]
  var userList=[]
  var userList2=[]
  var whole=0
  var hasNotLogin = [1]
  export default {
    name: 'Operator',
    data()
    {
      return{
        message: '',
        questions:data,
        leftList:leftList,
        userList:userList,
        userList2:userList2,
        isAllowAdmin,
        whole:whole,
        leftChosen:leftChosen,
        circleUrl: global.photo,
        hasNotLogin:hasNotLogin,
        myId:global.userId,
        myName:global.name,
        options: [{
          value: '是',
          label: '是'
    }, {
      value: '否',
      label: '否'
    }]
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
      console.log("Usercreated在执行")
      this.$axios.get(global.host + '/admin',
        {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          params: {
            action:"select",
            entity:"User",
          }
        })
        .then(function (response) {
          whole=response.data.length
          for (var i = 0; i < response.data.length; i++) {
            var l={
              userName:response.data[i].name,
              userId:response.data[i].id,
              mail:response.data[i].mail,
              allowAdmin:!response.data[i].cannotLogin,
              allowCommit:!response.data[i].cannotSpeak,
              allowAdmin2:!response.data[i].cannotLogin?"是":"否",
              allowCommit2:!response.data[i].cannotSpeak?"是":"否",
            }
           userList.push(l)
          }
        })
        .catch(function (error) {
          console.log(error);
        });
    },
    destroyed(){
      console.log("Userdestroyed在执行")
      userList=[]
      var hasNotLogin = [1]
    },
    methods: {
      chose:function(e){
        var i=e.target.getAttribute('data-item')
        console.log(e.target.getAttribute('data-item'))
        if(i==1){
          this.$router.push({path:'/OperatorClass'})
        }
        if(i==2){
          this.$router.push({path:'/OperatorLabel'})
        }
     },
      test:function(event){
        console.log(that.router)
      },
      getAdminValue:function(e){
        // var i=e.target.getAttribute('data-itemId')
        //console.log(e.target.getAttribute('data-itemId'))
        //console.log(2222)
        //console.log(userList[i].allowAdmin2)
        console.log(e)

      },
      getCommitValue:function(e){
        console.log(e)

      },
      deleteUser:function(e){
        var i=parseInt(e.target.getAttribute('data-itemId'))
       console.log(typeof (parseInt(i))+"-----"+i)
        this.$confirm('此操作将删除该用户, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.get(global.host + '/admin',
            {
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              params: {
                action: "delete",
                entity: "User",
                id:userList[i].userId
              }
            })
            .then(function (response) {
              console.log(response)
              location.reload()
            })
            .catch(function (error) {
              console.log(error);
            });
        })
         .catch(() => {
            });

      },
      confirm:function(e){
        var i=e.target.getAttribute('data-itemId')
        console.log(e.target.getAttribute('data-itemId'))
        console.log(userList[i])
        userList[i].allowAdmin=userList[i].allowAdmin2=="是"?1:0;
        userList[i].allowCommit=userList[i].allowCommit2=="是"?1:0;
        console.log(userList[i].userId)
        console.log(userList[i].allowAdmin?0:1)
        console.log(userList[i].allowCommit?0:1)

        this.$confirm('此操作将提交更改, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$axios.get(global.host + '/admin',
            {
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              params: {
                action: "update",
                entity: "User",
                id:userList[i].userId,
                cannotLogin:userList[i].allowAdmin?0:1,
                cannotSpeak:userList[i].allowCommit?0:1
              }
            })
            .then(function (response) {
              console.log(response)
              location.reload()
            })
            .catch(function (error) {
              console.log(error);
            });

        }).catch(() => {
          this.$message({

          });
        });
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
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
    margin-left:150px;
    width:200px;
    height:400px;
    background-color:white;
    text-align: center;
    display:inline-block;
    line-height: 60px;
    font-weight:500;
    position:fixed;
    box-shadow: 0 0 8px rgba(181, 185, 189, 0.6);
  }

  .contentBox{
    width:70%;
    height:auto;
    margin-top:100px;
    margin-left:380px;
    background-color:white;
    display:inline-block;
    line-height: 50px;
    vertical-align:top;
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



  table tr td{
    padding:20px;
    border-left: none;
    border-right: none;
    border-bottom:1px solid rgba(187,187,187,1)
  }
  table{
    border-collapse:collapse;
    width:100%;
    table-layout:fixed;
    word-wrap:break-word;
  }
  tr.firstLine{
    color:rgba(144,147,153,1)
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
  #classList li:hover
  {
    background-color: #f1f1f1;
    z-index:100;
    color:#bd5151;

  }
  .click:hover{
    cursor:pointer;
    text-decoration: underline;
  }
  .chosen{
    color:#bd5151;
  }


</style>
