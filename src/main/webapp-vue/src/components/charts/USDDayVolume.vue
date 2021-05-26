<template>
  <card>
    <template v-slot:header>
      USD Day Volume
    </template>
    <template v-slot:content>
      <v-chart
        class="chart"
        :option="option"
        v-bind:autoresize="true"
      />

    </template>
  </card>
</template>

<script>
import Card from "@/components/Card.vue";

export default {
  name: "USDDayVolume",
  props: {
    data : {
      type: Object
    }
  },
  components: {
    Card,
  },
  computed: {
    option() {
      return  {
        color: "#1ebc7d",
        tooltip: {
          trigger: "item",
          formatter: function (params, ticket, callback) {
              callback(ticket,  `<b>${params.name}</b> : $${parseInt(params.value).toLocaleString('en-US')}`);
          },
        },
        xAxis: {
          type: "category",
          data: this.data.days,
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: "value",
        },
        grid: { containLabel: true },

        series: [
          {
            data: this.data.volumes,
            type: "line",
            smooth: true,
            areaStyle: {
              color: {
                type: "linear",
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  { offset: 0, color: "rgba(30, 188, 125, 0.5)" },
                  { offset: 1, color: "rgba(43, 40, 62, 0.5)" },
                ],
              },
            },
          },
        ],
      }
    }
  }
};
</script>

<style scoped lang="scss">
.chart {
  height: 400px;
}
</style>
