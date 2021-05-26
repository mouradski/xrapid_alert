<template>
  <div class="api">
    <h2>Discover</h2>
    <div>
      Experience the Utility-Scan API. Whether for fun, research, or to build your own ODL models on your website / favorite social network, the Utility-scan API gives you full access to our database and real-time data over websocket.
    </div>

    <h3>
      Request your API Key then confirm payment with the XUMM application
    </h3>

    <div
      v-if="!loading && !qrCodeUrl"
      class="form"
    >
      <div class="group">
        <div>
          <label
            for="validity"
            aria-describedby="validity"
          >Validity in days</label>
          <input
            placeholder="Validity in days"
            name="validity"
            type="number"
            v-model="validity"
          />
        </div>
        <div>
          <label
            for="renew"
            aria-describedby="renew"
          >is renew ?</label>
          <input
            name="renew"
            type="checkbox"
            v-model="renew"
          />
        </div>
      </div>
      <div
        v-if="renew"
        class="group"
      >
        <div class="apiKey">
          <label
            for="apiKey"
            aria-describedby="apiKey"
          >Api Key</label>
          <input
            name="apiKey"
            type="text"
            v-model="apiKey"
          />
        </div>
      </div>

      <button @click="getQRCode">{{renew ? 'Renew API Key' : 'Request API Key' }} </button>
    </div>
    <div v-else-if="qrCodeUrl && !loading">
      <img
        class="qr"
        :src="qrCodeUrl"
        width="256"
        height="256"
      >
    </div>
    <div
      v-else
      class="loading"
    >
      <div class="lds-ripple">
        <div></div>
        <div></div>
      </div>
    </div>

    <div class="instructions">
      <card>
        <template v-slot:header>
          Get Transactions </template>
        <template v-slot:content>
          <div class="code-wrapper">
            <code>
              <span class="green">GET</span>
              https//api.utility-scan.com/odl?key=API_KEY&source=USD&destination=MXN&from=20-01-2020&to=20-02-2020&page=1&size=50
            </code>
          </div>
        </template>
      </card>
      <card>
        <template v-slot:header>
          Get Transactions as CSV file </template>
        <template v-slot:content>
          <div class="code-wrapper">
            <code>
              <span class="green">GET</span>
              https//api.utility-scan.com/csv?key=API_KEY&source=USD&destination=MXN&from=20-01-2020&to=20-02-2020&page=1&size=50
            </code>
          </div>
        </template>
      </card>
      <card>
        <template v-slot:header>
          Get Stats </template>
        <template v-slot:content>
          <div class="code-wrapper">
            <code>
              <span class="green">GET</span>
              https://api.utility-scan.com/odl/stats?key=API_KEY
            </code>
          </div>
        </template>
      </card>
      <card>
        <template v-slot:header>
          Get ODLs via WebSockets </template>
        <template v-slot:content>
          <div class="ws code-wrapper">
            <pre>
        let socket = new SockJS('https://api.utility-scan.com/websocket');
        let client = Stomp.over(socket);
        client.connect({}, function () {
                client.subscribe('/top/odl', function (odlPayment) {
                    console.log(odlPayment);
                }, {apiKey: "API_KEY"})
            }
         );
         </pre>
          </div>
        </template>
      </card>

    </div>

  </div>
</template>
<script>
import Card from "@/components/Card.vue";
import { HTTP } from "@/http";

export default {
  name: "Api",
  components: { Card },
  data() {
    return {
      validity: 30,
      renew: false,
      apiKey: "",
      qrCodeUrl: false,
      paymentId: false,
      loading: false,
    };
  },
  created() {},
  methods: {
    getQRCode() {
      this.loading = true;
      HTTP.get(
        `api/xumm?days=${this.validity}${
          this.renew && this.apiKey ? "&key=" + this.apiKey : ""
        }`
      ).then((response) => {
        this.qrCodeUrl = response.data.qrCodeUrl;
        this.paymentId = response.data.paymentId;
        this.$nextTick(() => {
          this.loading = false;
        });
        const checkPayment = setInterval(() => {
          HTTP.get(`api/xumm/${this.paymentId}}`).then((response) => {
            if (!["REJECTED", "WAITING"].includes(response.data.key)) {
              //Valid
              this.apiKey = response.data.key;
              clearInterval(checkPayment);
            } else if (response.data.key === "REJECTED") {
              this.apiKey = "";
              this.qrCodeUrl = false;
              clearInterval(checkPayment);
            }
          });
        }, 2000);
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.api {
  max-width: 950px;
  margin-left: auto;
  margin-right: auto;
  margin-top: 50px;
  display: flex;
  flex-wrap: wrap;
  flex-direction: column;
  justify-content: center;
  align-items: center;

  & > * {
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    flex-direction: column;
  }
}

input {
  background: #2b283e;
  border: none;
  color: white;
  border-radius: 5px;
  height: 30px;
}

.apiKey {
  width: 100%;
}

.qr {
  border-radius: 10px;
}

input[type="text"] {
  width: 50%;
}

.instructions {
  margin-top: 50px;
  display: flex;
  flex-wrap: wrap;
  flex-direction: column;

  & > div {
    margin: 15px;
  }
}

.group {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;

  & > div {
    display: flex;
    flex-direction: column;
    margin: 10px;
    align-items: center;
  }
}

button {
  padding: 10px;
  border-radius: 5px;
  border: none;
  font-weight: 700;
  margin: 10px;
  cursor: pointer;
  height: 30px;
  line-height: 0px;
  background: #1ebc7d;
  color: #fff;
}

@media screen and (max-width: 768px) {
  .code-wrapper {
    overflow: auto !important;
    width: 300px;
  }

  code {
    overflow: auto;
    white-space: pre;
  }
}

.ws {
  display: flex;
  justify-content: center;
  flex-direction: column;

  & > pre {
    text-align: left;
  }
}

label {
  font-size: 16px;
  font-weight: 400;
  width: 100%;
  margin: 10px;
}

.green {
  color: #1ebc7d;
}

/****Loading*/

.loading {
  height: 256px;
  width: 256px;
  background: #2b283e;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.lds-ripple {
  display: inline-block;
  position: relative;
  width: 80px;
  height: 80px;
}
.lds-ripple div {
  position: absolute;
  border: 4px solid #1ebc7d91;
  opacity: 1;
  border-radius: 50%;
  animation: lds-ripple 1s cubic-bezier(0, 0.2, 0.8, 1) infinite;
}
.lds-ripple div:nth-child(2) {
  animation-delay: -0.5s;
}
@keyframes lds-ripple {
  0% {
    top: 36px;
    left: 36px;
    width: 0;
    height: 0;
    opacity: 1;
  }
  100% {
    top: 0px;
    left: 0px;
    width: 72px;
    height: 72px;
    opacity: 0;
  }
}
</style>
