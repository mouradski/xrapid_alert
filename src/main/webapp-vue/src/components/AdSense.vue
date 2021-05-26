<template>

  <ins
    :class="{'vertical' : vertical, 'adsbygoogle' : true, left : position === 'left', right : position === 'right', mobile}"
    :data-ad-client="googleAdId"
    :data-ad-slot="dataSlot"
  ></ins>
</template>

<script>
export default {
  name: "AdSense",
  props: {
    timeout: Number,
    dataSlot: String,
    vertical: Boolean,
    position: String,
    mobile: Boolean,
  },
  data() {
    return {
      googleInit: null,
      googleAdId: "ca-pub-9921906109701931",
    };
  },
  mounted() {
    let timeout = 200;
    if (this.timeout) timeout = this.timeout;
    this.googleInit = setTimeout(() => {
      if (typeof window !== "undefined")
        (window.adsbygoogle = window.adsbygoogle || []).push({});
    }, timeout);
  },
  destroyed() {
    if (this.googleInit) clearTimeout(this.googleInit);
  },
};
</script>

<style scoped>
.adsbygoogle {
  border: none;
  display: inline-block;
  width: 728px !important;
  height: 90px !important;
}

.vertical {
  width: 160px !important;
  height: 600px !important;
  position: fixed;
}

.mobile {
  width: 336px !important;
  height: 280px !important;
}

.left {
  left: 0;
}

.right {
  right: 0;
}
</style>

