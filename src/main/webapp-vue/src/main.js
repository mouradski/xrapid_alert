import Vue from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'

Vue.config.productionTip = false


import ECharts from 'vue-echarts'
import { use } from 'echarts/core'

import {
  CanvasRenderer
} from 'echarts/renderers'
import {
  BarChart,
  LineChart
} from 'echarts/charts'
import {
  GridComponent,
  TooltipComponent,
  LegendComponent
} from 'echarts/components'

import {
  Lines3DChart
} from 'echarts-gl/charts';
import {
  GlobeComponent
} from 'echarts-gl/components';

use([
  CanvasRenderer,
  BarChart,
  LineChart,
  Lines3DChart,
  GlobeComponent,
  GridComponent,
  TooltipComponent,
  LegendComponent
]);

Vue.component('v-chart', ECharts)

Vue.filter('durationFormat', function (value) {

  let minutes = Math.floor(value / 60);

  let seconds = value - minutes * 60;

  if (minutes == 0) {
    if (seconds < 3) {
      return 'Just Now';
    } else {
      return seconds + " sec ago";
    }
  } else {

    if (minutes < 2) {
      if (seconds < 2) {
        return minutes + ' min and ' + seconds + ' sec ago';
      } else {
        return minutes + ' min and ' + seconds + ' sec ago';
      }
    } else {
      if (seconds < 2) {
        return minutes + ' min and ' + seconds + ' sec ago';
      } else {
        return minutes + ' min and ' + seconds + ' sec ago';
      }
    }
  }


});

new Vue({
  router,
  store,
  render: function (h) { return h(App) }
}).$mount('#app')
