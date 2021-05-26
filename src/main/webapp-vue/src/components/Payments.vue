<template>
  <card>
    <template v-slot:header>
      Last XRPL transactions spotted as ODL
    </template>
    <template v-slot:content>
      <div class="table-wrapper">
        <table>
          <thead>
            <th
              v-for="(header,index) in headersToDisplay"
              :key="index"
            >
              {{header.label}}
            </th>
            <th>
            </th>
          </thead>
          <tbody
            v-for="(row,index) in filteredData"
            :key="index"
          >
            <tr
              class="row"
              @click="toggle(index)"
            >
              <td
                v-for="header in headersToDisplay"
                :key="header.value"
                :data-label="header.label"
              >
                {{`${header.format ? header.format(row[header.value]) : row[header.value]}`}}
              </td>
              <td v-if="!isMobile">
                <i class="gg-more-vertical-alt"></i>
              </td>
            </tr>
            <transition name="slide-fade">
              <td
                colspan="8"
                v-show="expanded === index"
              >
                <tr>
                  <table class="details">
                    <tbody>
                      <tr>
                        <td
                          v-for="header in detailsHeaders"
                          :key="header.value"
                          :data-label="header.label"
                        >

                          <span v-if="header.link">
                            <a
                              :href="header.link(row[header.value])"
                              target="_blank"
                            >{{`${header.format ? header.format(row[header.value]) : row[header.value]}`}}</a>
                          </span>
                          <span v-else>{{`${header.format ? header.format(row[header.value]) : row[header.value]}`}}</span>

                        </td>
                      </tr>
                    </tbody>
                  </table>
                </tr>
              </td>
            </transition>

          </tbody>
        </table>
      </div>

      <div class="pagination">
        <button
          v-if="page > 0"
          @click="() => page = page - 1"
          class="gg-chevron-left"
        >

        </button>
        <button
          :class="p === page && 'current-page'"
          v-for="p in nbPages"
          :key="p"
          v-show="(p < page + 3) && (p > page - 3)"
          @click="() => page = p"
        >{{ p + 1 }}
        </button>

        <button
          v-if="page < nbPages.length"
          @click="() => page = page + 1"
          class="gg-chevron-right"
        ></button>
      </div>

    </template>
  </card>
</template>

<script>
import Card from "@/components/Card.vue";

export default {
  name: "Summary",
  props: {
    data: {
      type: Array,
      default: [],
    },
  },
  data() {
    return {
      page: 0,
      limit: 20,
      detailsHeaders: [
        {
          value: "transactionHash",
          label: "TRANSACTION HASH",
          link: (v) => "https://bithomp.com/explorer/" + v,
          format: (v) => (this.isMobile ? v.substring(0, 10) + "..." : v),
        },
        {
          value: "xrpToFiatTradeIds",
          label: "SELL TRADES",
          format: (v) => (!!v ? v.join(", ") : " - "),
        },
        {
          value: "fiatToXrpTradeIds",
          label: "BUY TRADES",
          format: (v) => (!!v ? v.join(", ") : " - "),
        },
        {
          value: "spottedAt",
          label: "SPOTTED AT",
        },
      ],
      headers: [
        {
          value: "dateAsString",
          label: "DATE",
          format: (v) => new Date(v).toLocaleString("en-US"),
          mobile: true,
        },
        {
          value: "amount",
          label: "AMOUNT",
          format: (v) => `${v.toFixed(3)} XRP`,
        },
        {
          value: "usdValue",
          label: "USD VALUE",
          format: (v) => `$${v.toFixed(3)}`,
          mobile: true,
        },
        { value: "source", label: "FROM" },
        { value: "destination", label: "TO" },
        { value: "sourceFiat", label: "SOURCE FIAT", mobile: true },
        { value: "destinationFiat", label: "TARGET FIAT", mobile: true },
      ],
      expanded: false,
    };
  },
  components: {
    Card,
  },
  computed: {
    filteredData() {
      return this.data.slice(this.page * this.limit).slice(0, this.limit);
    },
    nbPages() {
      return [...Array(parseInt(this.data.length / this.limit)).keys()];
    },
    headersToDisplay() {
      return this.headers.filter(
        (h) => (this.isMobile && h.mobile) || !this.isMobile
      );
    },
    isMobile() {
      if (
        /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(
          navigator.userAgent
        )
      ) {
        return true;
      } else {
        return false;
      }
    },
  },
  methods: {
    toggle(index) {
      this.expanded = this.expanded === index ? false : index;
    },
  },
};
</script>

<style scoped lang="scss">
@import url("https://css.gg/more-vertical-alt.css");
@import url("https://css.gg/chevron-right.css");
@import url("https://css.gg/chevron-left.css");

table {
  margin-left: auto;
  margin-right: auto;
  border-collapse: collapse;
  border-style: hidden;
  min-width: 80%;
  max-width: 80%;
}
th,
td {
  padding: 10px;
}

th {
  font-size: 14px;
}

td a {
  color: #1ebc7d;
}

tr.row:hover {
  background-color: #f5f5f52c;
}

td {
  border-bottom: 1px solid #6e7079;
  color: #cccccc;
}

.table-wrapper {
  display: block;
  width: 100%;
  overflow-x: auto;
}

.gg-more-vertical-alt {
  color: #1ebc7d;
}

.gg-chevron-right,
.gg-chevron-left {
  height: 0;
  display: inline-flex !important;
}

.pagination button {
  display: inline-flex;
  position: relative;
  margin-right: 4px;
  justify-content: center;
  align-content: center;
  background-color: transparent;
  font-weight: 600;
  border: none;
  color: white;
  cursor: pointer;
  width: 32px;
  padding: 10px;
  border-radius: 8px;
}

.pagination button.current-page {
  background-color: #1ebc7d;
}

.pagination button:hover {
  background-color: #e0e0e01e;
}

.slide-fade-enter-active {
  transition: all 0.4s ease;
}
.slide-fade-leave-active {
  transition: all 0.1s ease;
}
.slide-fade-enter, .slide-fade-leave-to
{
  transform: translateY(10px);
  opacity: 0;
}

@media screen and (max-width: 600px) {
  table {
    border: 0;
    width: 100%;
    table-layout: fixed;
  }

  table caption {
    font-size: 1.3em;
  }

  table thead {
    border: none;
    clip: rect(0 0 0 0);
    height: 1px;
    margin: -1px;
    overflow: hidden;
    padding: 0;
    position: absolute;
    width: 1px;
  }

  table tr {
    border-bottom: 3px solid #1ebc7d;
    display: block;
    margin-bottom: 0.625em;
  }

  table td {
    border-bottom: 1px solid #ddd;
    display: block;
    font-size: 0.8em;
    text-align: right;
  }

  table td::before {
    content: attr(data-label);
    float: left;
    font-weight: bold;
    text-transform: uppercase;
  }

  table td:last-child {
    border-bottom: 0;
  }
}

/*DETAILS*/

table.details {
  border: 0;
  width: 100%;
  min-width: 100%;
  table-layout: fixed;
}

table.details caption {
  font-size: 1.3em;
}

table.details thead {
  border: none;
  clip: rect(0 0 0 0);
  height: 1px;
  margin: -1px;
  overflow: hidden;
  padding: 0;
  position: absolute;
  width: 1px;
}

table.details tr {
  border-bottom: none !important;
  display: block;
  margin-bottom: 0.625em;
}

table.details td {
  border-bottom: 1px solid #ddd;
  display: block;
  font-size: 0.8em;
  text-align: right;
}

table.details td::before {
  content: attr(data-label);
  float: left;
  font-weight: bold;
  text-transform: uppercase;
}

table.details td:last-child {
  border-bottom: 0;
}
</style>
