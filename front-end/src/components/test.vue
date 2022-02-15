<template>
  <div id="body" :style="bg">
    <div class="top">
      <ul>
        <li class="link01">Q/A SYSTEM</li>
        <li><a href="#" id="link03"><i class="iconfont">&#xe625;</i>&nbsp&nbsp主页</a></li>
        <li class="link02"><a href="#"><i class="iconfont">&#xe7bf;</i>&nbsp&nbsp提问</a></li>
        <li class="link02">
          <a href="#"><i class="iconfont">&#xe627;</i>&nbsp&nbsp社区</a>
        </li>

        <div class="search bar">
          <form>
            <input type="text" placeholder="请输入您要搜索的内容...">
            <button type="submit"></button>
          </form>
        </div>
        <div class="buBox">
          <!-- 触发按钮 -->
          <button id="triggerBtn"><li><a href="#"><i class="iconfont">&#xe601;</i></a></li></button>

        </div>
      </ul>
    </div>
    <!--导航栏end-->

    <div id="classList" class="listContainer" >
      <li v-for="item,index in leftList" class="ui-link" @click="chose" :data-item="index">
        {{item}}
      </li>
    </div>
    <!--分类栏 end-->
    <div id ="mainContent" class="contentBox">
      <el-table
        :data="userList"
        height="725"
        border
        style="width: 100%">
        <template slot-scope="scope">
        <el-table-column
          type="index"
          width="50">
        </el-table-column>
        <el-table-column
          prop="userName"
          label="用户名"
          width="180">
        </el-table-column>
        <el-table-column
          prop="mail"
          label="邮箱"
          width="180">
        </el-table-column>
        <el-table-column
          prop="allowAdmin"
          label="是否允许登录">
          <el-select v-if="scope.allowAdmin"  @change='getAdminValue'  v-model="scope.allowAdmin2"
                     placeholder="是">
            <el-option
              v-for="item2 in options"
              :key="item2.value"
              :label="item2.label"
              :value="item2.value">
            </el-option>

          </el-select>
          <el-select v-else    @change='getAdminValue'  v-model="scope.allowAdmin2"
                     placeholder="否">
            <el-option
              v-for="item2 in options"
              :key="item2.value"
              :label="item2.label"
              :value="item2.value">
            </el-option>
          </el-select>
        </el-table-column>
        <el-table-column
          prop="allowAdmin"
          label="是否允许发言">
          <el-select v-if="scope.allowCommit"  @change='getAdminValue'  v-model="scope.allowCommit2"
                     placeholder="是">
            <el-option
              v-for="item2 in options"
              :key="item2.value"
              :label="item2.label"
              :value="item2.value">
            </el-option>

          </el-select>
          <el-select v-else    @change='getAdminValue'  v-model="scope.allowAdmin2"
                     placeholder="否">
            <el-option
              v-for="item2 in options"
              :key="item2.value"
              :label="item2.label"
              :value="item2.value">
            </el-option>
          </el-select>
        </el-table-column>
        <el-table-column label="操作">

        </el-table-column>
        </template>
      </el-table>


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
  var userList=[]

  export default {
    name: 'Operator',
    data()
    {
      return{
        message: '',
        questions:data,
        leftList:leftList,
        userList:userList,
        isAllowAdmin,
        options: [{
          value: '是',
          label: '是'
        }, {
          value: '否',
          label: '否'
        }],
        bg: {
          backgroundImage: "url(" + require("../assets/bg3.jpg") + ")",
          backgroundRepeat: "no-repeat",
          backgroundSize: "100% 100%",
        },

      }
    },

    created()
    {
      console.log("Usercreated在执行")
      this.$axios.get(global.host + '/test/admin',
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
          console.log(userList)
        })
        .catch(function (error) {
          console.log(error);
        });
    },
    destroyed(){
      console.log("Userdestroyed在执行")
      userList=[]
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
          this.$axios.get(global.host + '/test/admin',
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
          this.$axios.get(global.host + '/test/admin',
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
      handleEdit(index, row) {
        console.log(index, row);
      },
      handleDelete(index, row) {
        console.log(index, row);
      }

    }
  }
</script>

<style scoped>

  @import "http://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css";
  @import "../components/css/buttonBox.css";
  @import "../assets/icon/iconfont.css";
  @import "css/searchBar2.css";
  @import "../components/css/top.css";

  #body{
    width: 100%;
    height: 100%;
    margin:0;
  }
  .listContainer{
    margin-top:100px;
    margin-left:20px;
    width:200px;
    height:500px;
    background-color:white;
    text-align: center;
    display:inline-block;
    line-height: 60px;
    font-weight:500;
    position:fixed;
  }

  .contentBox{
    width:80%;
    height:auto;
    margin-top:100px;
    margin-left:250px;
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
    border:1px solid gray;
    padding:10px;

  }
  table{
    border-collapse:collapse;
    width:100%;
    table-layout:fixed;
    word-wrap:break-word;
  }
  tr.firstLine{
    background-color: lightGray;
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
    color:white;
    box-shadow:#bd5151 0px 0px 10px;
  }
  .click:hover{
    cursor:pointer;
    text-decoration: underline;
  }

</style>
