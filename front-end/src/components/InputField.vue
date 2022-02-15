<template>
  <div class="InputField">
    <div id="editor2">

      <textarea id="content" style="display:none;">
        {{ content }}
      </textarea>
    </div>
  </div>
</template>

<script>
  import global from './global.vue'

export default {
  name: 'InputField',
  data () {
    return {
      content: '#请输入',
      editor:null,

    }
  },
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
    }
  },

  mounted :function () {
    var that = this
    console.log('-----------------');
    this.editor = editormd("editor2", {
      width: "100%",
      height: "100%",
      // width: "90%",
      // height: '740px',
      path: "../../static/editor.md-master/lib/",
      // theme : "dark",
      // previewTheme : "dark",
      // editorTheme : "pastel-on-dark",
      markdown : this.content,
      codeFold : true,
      //syncScrolling : false,
      saveHTMLToTextarea : true,    // 保存 HTML 到 Textarea
      searchReplace : true,
      //watch : false,                // 关闭实时预览
      htmlDecode : "style,script,iframe|on*",            // 开启 HTML 标签解析，为了安全性，默认不开启
      toolbar  : false,             //关闭工具栏
      //previewCodeHighlight : false, // 关闭预览 HTML 的代码块高亮，默认开启
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
      onload : function() {
        console.log('onload', this);
        editormd.loadPlugin("../../static/editor.md-master/plugins/image-handle-paste/image-handle-paste", function(){
          that.editor.imagePaste();
        });
        //this.fullscreen();
        //this.unwatch();
        //this.watch().fullscreen();

        //this.setMarkdown("#PHP");
        //this.width("100%");
        //this.height(480);
        //this.resize("100%", 640);
      }
    });
  },
  updated: function () {
    this.editor.width = this.width;
    this.editor.height = this.height;
  }
}


</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
