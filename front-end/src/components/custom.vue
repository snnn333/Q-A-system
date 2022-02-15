<template>
  <div class="functional-select-wrapper" v-on:click.stop="focus()">
  <label class="display-container multiple-select-container clearfix" v-bind:class="(show)?'single-selected-focus':''">
    <p v-show="selectedList.length == 0 ">{{ noDataMess }}</p>
    <p class="multiple-selected-item" v-for="item in selectedList" track-by="$index">{{ item }}<i v-on:click.stop.prevent="remove($index)">×</i></p>
    <i class="drop" v-bind:class="(show)?'drop-up':''">▼</i>
    </label>
  <div class="options-container" v-show="show">
    <div class="search-container">
      <input placeholder="在这里填写内容" class="search-input" v-model="search" v-on:keyup="doSearch($event)" v-on:click.stop/>
      </div>
    <ul class="options-ul-list" v-show="search!=''">
      <li v-for="item in optionsList" v-on:click.stop="select(item)">{{ item }}</li>
      </ul>
    </div>
  </div>
</template>

<script>
    export default {
        name: "custom-multiple-input",
      props: ['selecteddata', 'noDataMess'],
      data() {
        return{
          optionsList: this.selecteddata,
          show: false,
          search: "",
          selectedList: [],
          // noDataMess: ""
        }
      },
      computed:{
        // selectedFocus:function(){
        // 	return {
        // 		"single-selected-focus": this.show
        // 	}
        // },
        // dropUp: function(){
        // 	return {
        // 		"drop-up": this.show
        // 	}
        // }
      },
      ready: function(){
        window.addEventListener('click',this.blur)
      },
      // events: {
      // 	// ajax获取originOptions后 初始化
      // 	initCustomMultiple: function(data){
      // 		this.selectedList = data
      // 	}

      // },
      watch: {

        selecteddata: function(val, oldVal){
          console.log('selected old: ' + JSON.stringify(oldVal))
          console.log('selectednew: ' + JSON.stringify(val))
          this.selectedList = val
        }

      },
      methods:{
        focus: function(){
          if (!this.show){
            document.body.click();
            // console.log('自定义输入 下拉!!!')
            var mySelf = this;
            mySelf.show = true;
            mySelf.searchInputFocus();
          }
          else{
            this.blur();
          }
        },
        searchInputFocus: function(){
          var searchInput = this.$el.getElementsByClassName('search-input')[0];

          this.$nextTick(function(){
            searchInput.focus();
          })

        },
        select: function(name){
          var mySelf = this;
          var search = mySelf.search;
          var selectedList = mySelf.selectedList;

          //若selectedIdList存在该search 则删除   不存在 则添加
          var index = selectedList.indexOf(name);
          // console.log(index)
          // console.log(equipmentName)
          // console.log(JSON.stringify(selectedList))
          // console.log(index)
          if (index !=-1 ){
            mySelf.remove(index);
            return;
          }
          else{
            selectedList.push(search);
            mySelf.initSearch();
            mySelf.optionsList = [];
            mySelf.searchInputFocus();
            mySelf.dispatchData();
          }
        },
        dispatchData: function(){
          //console.log('派发啊！！')
          this.$emit('inputed', this.selectedList)
          //this.$dispatch('inputed', this.selectedList)
        },
        remove: function(index){
          var mySelf = this;
          var selectedList = mySelf.selectedList;

          selectedList.splice(index,1);
          mySelf.initSearch();
          mySelf.optionsList = [];
          mySelf.searchInputFocus();
          mySelf.dispatchData();
        },
        doSearch: function(event){
          var mySelf = this;
          var search = mySelf.search;
          var selectedList = selectedList;

          mySelf.optionsList = []
          mySelf.optionsList.unshift(search)

          // console.log(JSON.stringify(search))
          // console.log(JSON.stringify(mySelf.optionsList))
          // console.log(JSON.stringify(mySelf.recordModal.equipment.optionsList))

          if (event.keyCode==13){
            mySelf.select(search)
            // mySelf.initSearch();
            // optionsList = []
          }
        },
        initSearch: function(){
          var mySelf = this;
          mySelf.search = '';
        },
        blur: function(){
          this.show = false;
          this.search = '';
        }
      },
    }
</script>

<style scoped>
  @import "checkbox/css/select-vue-component.css";
/*  @import "http://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css";
  @import "https://cdn.staticfile.org/twitter-bootstrap/4.1.0/css/bootstrap.min.css";*/

  * {
    padding: 0;
    margin: 0;
  }

  *, *:before, *:after {
    -webkit-box-sizing: border-box;
    -moz-box-sizing: border-box;
    box-sizing: border-box;
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
  .btns button:focus{
    outline: none;
  }

  .input-group .form-control{
    height: 100px;
    weight:30%;
  }

</style>
