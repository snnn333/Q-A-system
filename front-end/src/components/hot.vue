<template>
  <div class="hot">
  <div class="cque" v-for="item,index in queHot" tabindex="0">
    <h4 class="card-title">{{item.title}}</h4>
    <p ></p>
    <el-dialog
            title="提示"
            :visible.sync="dialogVisible"
            width="30%"
            :before-close="handleClose">
      <span>查看问题需要登录，是否前往登录</span>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="toLogin">确 定</el-button>
  </span>
    </el-dialog>

    <a href="#" class="card-link" :data-itemId="index" @click="toQuestionDetail">详情</a>
    <a href="#" class="card-link" :data-itemId="index" @click="toQuestionDetail">回答</a>
    <p class="card-text"><small class="text-muted">{{item.author}} 发表于 {{item.time}}</small></p>
  </div>
  </div>
</template>

<script>
  import util from '../util.js'
  import global from './global.vue'
  let queHot = []
    export default {
        name: "hot",
      data(){
        return{
          queHot:queHot,
          dialogVisible:false
        }
      },
      created() {
        //获取热帖
        this.$axios(
          {
            method:'get',
            url:global.host+'/main',
            params:{
              action:"select",
              entity:"Question",
              QuestionType: encodeURI("热帖"),
              index: 0,
              num: 50,
              orderBy:'frequency'
            }
          })
          .then(res =>{
            console.log(res)
            console.log()
            for(let i = 0; i < res.data.length; i++)
            {
              var l = {
                title: res.data[i].title,
                content:res.data[i].content,
                author:res.data[i].name,
                time:util.formatTime(res.data[i].time),
                questionId: res.data[i].id,
                userId: res.data[i].userId
              }
              queHot.push(l)
            }
            console.log("------quehot-------")
            console.log(queHot)
          })
      },
      destroyed(){
        console.log("QueHot has been destroyed")
        queHot.splice(0,queHot.length)
      },
      methods:{
        toQuestionDetail:function (e){
          if(global.userId===-1){
            this.dialogVisible=true;
          }
          else {
            var i = e.target.getAttribute('data-itemId')
            console.log(e.target.getAttribute('data-itemId'))
            this.$router.push({
              path: '/QuestionDetail',
              query: {
                questionId: queHot[i].questionId,
                userId: queHot[i].userId
              }
            })
          }
        },
        toLogin:function(){
          this.$router.push({
            path: '/Login',
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
  .cque{
    border:1px solid #dee2e6;
    padding:3%;
    margin: 5px 5px 5px 0px ;
    background: #FFFFFF;
    outline-color: #bd5151;
    /* border-color: rgba(189,81,81,.8);*/
    box-shadow: 0 0 8px rgba(181, 185, 189, 0.6);
  }
  .card-link{
    color: #bd5151;
    border: none;
    display: inline-block;
    padding: 0;
  }
  .card-title {
    margin-bottom: .75rem;
  }
  h4{
    font-size: 1.5rem;
    font-family: inherit;
    font-weight: 500;
    line-height: 1.2;
    color: inherit;
    margin-top: 0;
    display: block;
    -webkit-margin-before: 1.33em;
    -webkit-margin-after: 1.33em;
    -webkit-margin-start: 0px;
    -webkit-margin-end: 0px;
  }

</style>
