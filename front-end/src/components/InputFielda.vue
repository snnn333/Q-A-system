
软件学院―刘鹏飞 2019/7/17 15:50:11
<template>
  <div class="InputFielda">
    <div id="editor">

      <textarea     style="display:none;" >
      </textarea>
    </div>
  </div>
</template>

<script>
  import global from './global.vue'


  export default {
    name: 'InputFielda',
    data () {
      return {
        value: '',
        editor:null,
      }
    },
    props: {
      'toolbar': Boolean,
      'content': null,
      'previewing':Boolean,
      'hide':Boolean
    },

    // created:{
    //   this.value=this.content
    // },

    methods: {
      setSize: function (width, height) {
        this.editor.width = width;
        this.editor.height = height;
      },

      SetToolBar: function(value){
        this.editor.toolbar = value;
      },


      commit: function () {
        // $.post('http://localhost:8082/test/')；
      },
      // getMarkDown: function () {
      //   this.value = document.getElementById("content").innerText
      // }
    },
    updated(){
      console.log(this.content)
      // this.value = this.content
      // this.editor.markdown = this.content

    },
    watch: {
      content:{
        deep:true,
        handler: function(newv, oldv){
          if(this.editor !== null){
            this.editor.setMarkdown(newv.value)
            this.editor.watch()
            this.$emit('loaded',this.editor.getHTML())
          }
          // this.editor.setMarkdown(newv.value)
        }
      },

    },
    mounted() {
      console.log(this.content)
      let that = this
      var md
      this.value = this.content
      md = editormd("editor", {
        width: "100%",
        // height: "100%",
        // width: "90%",
        height: '740',
        path: "../../static/editor.md-master/lib/",
        // theme : "dark",
        // previewTheme : "dark",
        // editorTheme : "pastel-on-dark",
        markdown : this.content.value,
        codeFold : true,
        // syncScrolling : false,
        saveHTMLToTextarea : true,    // 保存 HTML 到 Textarea
        searchReplace : true,
        // watch : false,                // 关闭实时预览
        htmlDecode : "style,script,iframe|on*",            // 开启 HTML 标签解析，为了安全性，默认不开启
        toolbar  : this.toolbar,             //关闭工具栏
        // previewCodeHighlight : false, // 关闭预览 HTML 的代码块高亮，默认开启
        emoji : true,
        taskList : true,
        tocm            : true,         // Using [TOCM]
        tex : true,                   // 开启科学公式TeX语言支持，默认关闭
        flowChart : true,             // 开启流程图支持，默认关闭
        sequenceDiagram : true,       // 开启时序/序列图支持，默认关闭,
        //dialogLockScreen : false,   // 设置弹出层对话框不锁屏，全局通用，默认为true
        //dialogShowMask : false,     // 设置弹出层对话框显示透明遮罩层，全局通用，默认为true
        //dialogDraggable : false,    // 设置弹出层对话框不可拖动，全局通用，默认为true
        //dialogMaskOpacity : 0.4,    // 设置透明遮罩层的透明度，全局通用，默认值为0.1
        //dialogMaskBgColor : "#000", // 设置透明遮罩层的背景颜色，全局通用，默认为#fff
        imageUpload : true,
        imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL : global.host + "/uploadInEditor",
        // uploadCallbackURL:"",
        crossDomainUpload : false,
        onchange: function(){
          // console.log(this)
          // that.content.value = this.getMarkdown()
          that.$emit('change', this.getMarkdown())
          // document.getElementById("content").innerText = this.getMarkdown()
        },
        onload : function(response) {
          if(that.hide === true){
            this.hide()
          }else if(that.previewing === true){
            this.previewing()
          }
          md.watch()
          that.$emit('loaded', this.getHTML())
          editormd.loadPlugin("../../static/editor.md-master/plugins/image-handle-paste/image-handle-paste", function(){
            md.imagePaste();
          });
          that.editor = md
        }
      });
    },

  }


</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
