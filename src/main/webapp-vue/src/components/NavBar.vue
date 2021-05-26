<template>
  <div class="nav">
    <router-link :to="'/'">
      <img
        height="38px"
        src="@/assets/logo.svg"
      />
    </router-link>

    <div
      v-show="!isMobile"
      class="right"
    >
      <router-link
        class="link green"
        :to="'/api'"
      >
        Get API</router-link>
      <a
        class="link white"
        target="_blank"
        href="https://www.xrptipbot.com/u:UtilityScan/n:twitter"
      ><img
          src="@/assets/xrp-symbol.png"
          width="20"
          height="20"
        >
        Tips</a>
    </div>
  </div>
</template>

<script>
export default {
  name: "NavBar",
  computed: {
    isMobile() {
      return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
        navigator.userAgent
      );
    },
  },
};
</script>

<style scoped lang="scss">
.gg-coffee {
  display: inline-block;
  margin: 0;
}

.nav {
  padding: 15px;
  position: relative;
}

.right {
  position: absolute;
  right: 0;
  top: 15px;

  & > a {
    padding: 10px;
    border-radius: 5px;
    font-weight: 700;
    margin: 5px;
    cursor: pointer;
    height: 45px;
    text-decoration: none;
    & > img {
      object-position: 0 4px;
    }
  }
}

.green {
  background: #1ebc7d;
  color: #fff;
}

.white {
  background: #fff;
  color: #211e34;
}
</style>
