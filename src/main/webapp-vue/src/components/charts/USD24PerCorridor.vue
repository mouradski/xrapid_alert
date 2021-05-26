<template>
  <card>
    <template v-slot:header>
      USD LAST 24H Volume Per Corridor
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
  name: "USD24PerCorridor",
  props: {
    data: {
      type: Object,
    },
  },
  components: {
    Card,
  },
  computed: {
    option() {
      return {
        color: "rgba(30, 188, 125, 0.8)",
        tooltip: {
          trigger: "item",
          formatter: function (params, ticket, callback) {
            callback(
              ticket,
              `<b>${params.name}</b> : $${parseInt(params.value).toLocaleString(
                "en-US"
              )}`
            );
          },
        },
        xAxis: {
          type: "category",
          data: Object.keys(this.data),
          axisLabel: {
            rotate: 45,
          },
        },
        yAxis: {
          type: "value",
        },
        grid: { containLabel: true },

        series: [
          {
            data: Object.values(this.data),
            type: "bar",
          },
        ],
      };
    },
  },
};
</script>


<style scoped lang="scss">
.chart {
  height: 400px;
}
</style>
