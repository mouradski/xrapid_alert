<template>
  <div class="summary">
    <card>
      <template v-slot:header>
        All Time Volume </template>
      <template v-slot:content>
        ${{datas.allTimeVolume && datas.allTimeVolume.toLocaleString('en-US')}}
      </template>
    </card>
    <card>
      <template v-slot:header>
        Since </template>
      <template v-slot:content>
        {{datas.allTimeFrom && new Date(datas.allTimeFrom).toLocaleString('en-US')}}
      </template>
    </card>
    <card>
      <template v-slot:header>
        Daily Volume ATH </template>
      <template v-slot:content>
        ${{datas.athDaylyVolume && datas.athDaylyVolume.toLocaleString('en-US')}}
      </template>
    </card>
    <card>
      <template v-slot:header>
        Last ODL spotted
      </template>
      <template v-slot:content>
        {{duration | durationFormat }}
      </template>
    </card>
  </div>

</template>

<script>
import Card from "@/components/Card.vue";

export default {
  name: "Summary",
  components: {
    Card,
  },
  props: {
    datas: {
      type: Object,
    },
  },
  data() {
    return {
      duration: 0,
    };
  },
  created() {
    setInterval(() => {
      this.duration =  Math.floor((new Date().getTime() - this.datas.lastODLSpotted) / 1000);     
    }, 1000);
  },
};
</script>

<style lang="scss">
.summary {
  flex: 1;
  max-width: 250px;
  display: flex;
  flex-direction: column;
  justify-content: center;

  & > div {
    padding-right: 15px;
    padding-left: 15px;
    margin: 15px;
  }
}

@media screen and (max-width: 1280px) {
  .summary {
    max-width: 100%;
    flex-direction: row;
    flex-wrap: wrap;

    & > div {
      width: 220px;
    }
  }
}

header {
  font-size: 16px !important;
  font-weight: 400 !important;
  margin-bottom: 10px;
}

.card-wrapper {
  font-weight: 600;
}
</style>
