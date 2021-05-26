<template>
  <div id="app">
    <NavBar />
    <router-view />
    <Footer />

  </div>
</template>
<script>
import NavBar from "@/components/NavBar.vue";
import Footer from "@/components/Footer.vue";

export default {
  name: "App",
  components: {
    NavBar,
    Footer,
  },
};
</script>

<style lang="scss">
body {
  margin: 0;
  padding: 0;
  background: #211e34;
}



#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #ececed;
}
</style>
