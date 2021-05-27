<template>
  <div class="home">

    <!--
    <AdSense
      :data-slot="'5026269580'"
      :timeout="1000"
      v-if="!isMobile"
    />
    -->

    <!-- mobile 1 -->
    <AdSense
      v-if="isMobile"
      :data-slot="'9421450790'"
      :timeout="1000"
      :mobile="true"
    />

    <!-- Les deux verticales -->
    <AdSense
      v-if="!isMobile"
      :data-slot="'5026269580'"
      :timeout="1000"
      :vertical="true"
      :position="'right'"
    />
    <AdSense
      v-if="!isMobile"
      :data-slot="'5026269580'"
      :timeout="1000"
      :vertical="true"
      :position="'left'"
    />

    <div class="panel">
      <Summary :datas.sync="summary" />
      <Globe ref="globe" />
    </div>

    <div class="volumes">
      <USDDayVolume :data="USDDayVolume" />
      <USD24PerCorridor :data="USD24PerCorridor" />
    </div>

    <AdSense
      v-if="!isMobile"
      :data-slot="'5026269580'"
      :timeout="1000"
    />

    <div class="payments">
      <Payments :data="payments" />
    </div>

    <!-- mobile 2 -->
    <AdSense
      v-if="isMobile"
      :data-slot="'9421450790'"
      :timeout="1000"
      :mobile="true"
    />

  </div>
</template>

<script>
import USDDayVolume from "@/components/charts/USDDayVolume.vue";
import Summary from "@/components/charts/Summary.vue";
import Payments from "@/components/Payments.vue";
import AdSense from "@/components/AdSense.vue";

import USD24PerCorridor from "@/components/charts/USD24PerCorridor.vue";
import Globe from "@/components/charts/Globe.vue";

import { HTTP } from "@/http";
import SockJS from "sockjs-client";
import Stomp from "webstomp-client";

export default {
  name: "Home",
  components: {
    USDDayVolume,
    Summary,
    USD24PerCorridor,
    Globe,
    Payments,
    AdSense,
  },
  data() {
    return {
      connected: false,
      USDDayVolume: {},
      USD24PerCorridor: {},
      payments: [],
      globalIndex: 0,
      stats: {},
      lastODLSpotted: Date.now(),
    };
  },
  created() {
    HTTP.get(`api/payments/stats`)
      .then((response) => {
        this.setStats(response.data);
      })
      .catch((e) => {
        this.errors.push(e);
      });

    HTTP.get(`api/payments`)
      .then((response) => {
        this.payments = response.data.sort((a, b) => (a.timestamp > b.timestamp) ? -1 : 0);

        this.lastODLSpotted = this.payments[0].timestamp;

        !!this.payments[this.globalIndex] &&
                      this.$refs.globe.addTransaction(
                        this.payments[this.globalIndex++]
                      );
        /*
        setInterval(() => {
          this.$nextTick(() => {
            !!this.payments[this.globalIndex] &&
              this.$refs.globe.addTransaction(
                this.payments[this.globalIndex++]
              );
          });
        }, 1500);
        */
      })
      .catch((e) => {
        this.errors.push(e);
      });

    this.socket = new SockJS(
      process.env.NODE_ENV === "production" ? "/ws" : "http://localhost:8080/ws"
    );
    this.stompClient = Stomp.over(this.socket);
    this.stompClient.connect(
      {},
      (frame) => {
        this.connected = true;
        this.stompClient.subscribe("/topic/payments", (message) => {
          console.log("Received from payments");
          console.log(message.body);
          let p = JSON.parse(message.body);
          this.lastODLSpotted = p.timestamp;

          this.payments.push(p);

          this.payments = this.payments.sort((a, b) => (a.timestamp > b.timestamp) ? -1 : 0);

          !!this.payments[this.globalIndex] &&
                        this.$refs.globe.addTransaction(
                          this.payments[this.globalIndex++]
                        );
        });

        this.stompClient.subscribe("/topic/stats", (message) => {
          console.log("Received from stats");
          console.log(message.body);
          this.setStats(JSON.parse(message.body));
        });
      },
      (error) => {
        console.log(error);
        this.connected = false;
      }
    );
  },
  computed: {
    summary() {
      return Object.assign({}, this.stats, {
        lastODLSpotted: this.lastODLSpotted,
      });
    },
    isMobile() {
      if (
        /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
          navigator.userAgent
        ) || window.innerWidth < 940
      ) {
        return true;
      } else {
        return false;
      }
    },
  },
  methods: {
    setStats(stats) {
      this.USDDayVolume = {
        days: stats.days,
        volumes: stats.last5DaysOdlVolume,
      };
      this.USD24PerCorridor = stats.topVolumes;
      this.stats = {
        athDaylyVolume: stats.athDaylyVolume,
        allTimeVolume: stats.allTimeVolume,
        allTimeFrom: stats.allTimeFrom,
      };
    },
  },
};

</script>

<style lang="scss">
.home {
  max-width: 1350px;
  margin-left: auto;
  margin-right: auto;
  margin-top: 50px;
}

@media only screen and (min-width: 2559px) {
  .home {
    max-width: 1800px;
  }
}

.volumes,
.payments {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-top: 50px;

  & > div {
    position: relative;
    width: 100%;
    flex: 1 0 40%;
    margin: 15px;

    @media screen and (max-width: 1280px) {
      flex: 1 0 45%;
    }
  }
}

.panel {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  flex-direction: row;

  @media screen and (max-width: 1280px) {
    align-items: center;
    flex-direction: column-reverse;
  }
}
</style>
