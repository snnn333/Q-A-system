<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  export default {
    name: 'App'
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
    width:100%;
    height: 100%;
    background: url("assets/pic/bg/4.png");

  }
  html,body,#app{
    width:100%;
    height: 100%;
    background-color: rgba(246,246,246,1);
    margin:0;
    padding:0;
  }
</style>
