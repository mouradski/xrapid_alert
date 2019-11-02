import { Component, OnInit } from "@angular/core";
import Chart from 'chart.js';
import * as Stomp from 'stompjs';
import {Client} from 'stompjs';
import * as SockJS from 'sockjs-client';
import {HttpClient} from '@angular/common/http';
import {Payment} from "../tables/tables.component";
import { CookieService } from 'ngx-cookie-service';



@Component({
  selector: "app-dashboard",
  templateUrl: "dashboard.component.html"
})
export class DashboardComponent implements OnInit {
  public canvas : any;
  public ctx;
  public datasets: any;
  public data: any;

  private client: Client;

  public myChart2: any;
  public myChart1: any;

  public lastTransaction:Payment;

  public trxSecondsAgo:number;

  public stats: Stats;

  constructor(private httpClient: HttpClient, private cookieService: CookieService) {
    const _this = this;

    this.trxSecondsAgo = 1;

    this.stats = new Stats();
    this.stats.allTimeFrom = '';
    this.lastTransaction = new Payment();
    this.httpClient.get<Stats>('/api/payments/stats').subscribe(data => {
      _this.stats = data;
      this.draw5DaysHistory(data.last5DaysOdlVolume);
      this.drawVolumesByCorridor(data.topVolumes);
    })

    httpClient.get<Payment[]>('/api/payments').subscribe(data => {
      _this.lastTransaction = data[data.length - 1];
      _this.trxSecondsAgo = 1;
    })

    const socket = new SockJS('/ws');
    this.client = Stomp.over(socket);

    this.client.connect({}, function (frame) {
      _this.client.subscribe('/topic/payments', function (message) {
        console.log(JSON.parse(message.body));
        _this.lastTransaction = JSON.parse(message.body);
        _this.trxSecondsAgo = 1;
      });

      _this.client.subscribe('/topic/stats', function (message) {

        _this.stats = JSON.parse(message.body);
        _this.updateStats(_this.stats)
      });
    });


    setInterval(function () {
      _this.trxSecondsAgo++;
    }, 1000)
  }

  updateStats(data: Stats) {
    this.myChart1.data.datasets[0].data = data.last5DaysOdlVolume;
    this.myChart2.data.datasets[0].labels = Object.keys(data.topVolumes);
    this.myChart2.data.datasets[0].data = Object.values(data.topVolumes);

    this.myChart1.update();
    this.myChart2.update();
  }

  ngOnInit() {

  }


  draw5DaysHistory(updatedData:Array<number>) {
    var gradientChartOptionsConfigurationWithTooltipRed: any = {
      maintainAspectRatio: false,
      legend: {
        display: false
      },

      tooltips: {
        backgroundColor: '#f5f5f5',
        titleFontColor: '#333',
        bodyFontColor: '#666',
        bodySpacing: 4,
        xPadding: 12,
        mode: "nearest",
        intersect: 0,
        position: "nearest"
      },
      responsive: true,
      scales: {
        yAxes: [{
          barPercentage: 1.6,
          gridLines: {
            drawBorder: false,
            color: 'rgba(29,140,248,0.0)',
            zeroLineColor: "transparent",
          },
          ticks: {
            suggestedMin: 60,
            suggestedMax: 125,
            padding: 20,
            fontColor: "#9a9a9a"
          }
        }],

        xAxes: [{
          barPercentage: 1.6,
          gridLines: {
            drawBorder: false,
            color: 'rgba(233,32,16,0.1)',
            zeroLineColor: "transparent",
          },
          ticks: {
            padding: 20,
            fontColor: "#9a9a9a"
          }
        }]
      }
    };

    this.canvas = document.getElementById("chart1");
    this.ctx = this.canvas.getContext("2d");

    var gradientStroke = this.ctx.createLinearGradient(0, 230, 0, 50);

    gradientStroke.addColorStop(1, 'rgba(233,32,16,0.2)');
    gradientStroke.addColorStop(0.4, 'rgba(233,32,16,0.0)');
    gradientStroke.addColorStop(0, 'rgba(233,32,16,0)'); //red colors

    var data = {
      labels: ['D-5', 'D-4', 'D-3', 'D-2', 'D-1', 'D'],
      datasets: [{
        label: "Data",
        fill: true,
        backgroundColor: gradientStroke,
        borderColor: '#ffb422',
        borderWidth: 2,
        borderDash: [],
        borderDashOffset: 0.0,
        pointBackgroundColor: '#ffb422',
        pointBorderColor: 'rgba(255,255,255,0)',
        pointHoverBackgroundColor: '#ffb422',
        pointBorderWidth: 20,
        pointHoverRadius: 4,
        pointHoverBorderWidth: 15,
        pointRadius: 4,
        data: [80, 100, 70, 80, 120, 80],
      }]
    };

    if (updatedData) {
      data.datasets[0].data = updatedData;
    } else {
      data.datasets[0].data = [0,0,0,0,0,0];
    }


    this.myChart1 = new Chart(this.ctx, {
      type: 'line',
      data: data,
      options: gradientChartOptionsConfigurationWithTooltipRed
    });

  }

  drawVolumesByCorridor(updatedData:Map<string, number>) {
    var gradientBarChartConfiguration: any = {
      maintainAspectRatio: false,
      legend: {
        display: false
      },

      tooltips: {
        backgroundColor: '#f5f5f5',
        titleFontColor: '#333',
        bodyFontColor: '#666',
        bodySpacing: 4,
        xPadding: 12,
        mode: "nearest",
        intersect: 0,
        position: "nearest"
      },
      responsive: true,
      scales: {
        yAxes: [{

          gridLines: {
            drawBorder: false,
            color: 'rgba(29,140,248,0.1)',
            zeroLineColor: "transparent",
          },
          ticks: {
            suggestedMin: 60,
            suggestedMax: 120,
            padding: 20,
            fontColor: "#9e9e9e"
          }
        }],

        xAxes: [{

          gridLines: {
            drawBorder: false,
            color: 'rgba(29,140,248,0.1)',
            zeroLineColor: "transparent",
          },
          ticks: {
            padding: 20,
            fontColor: "#9e9e9e"
          }
        }]
      }
    };
    this.canvas = document.getElementById("chart2");
    this.ctx = this.canvas.getContext("2d");

    var gradientStroke = this.ctx.createLinearGradient(0, 230, 0, 50);

    gradientStroke.addColorStop(1, 'rgba(233,32,16,0.2)');
    gradientStroke.addColorStop(0.4, 'rgba(233,32,16,0.0)');
    gradientStroke.addColorStop(0, 'rgba(233,32,16,0)'); //red colors

    var data = {
      labels: ['JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'],
      datasets: [{
        label: "Data",
        fill: true,
        backgroundColor: gradientStroke,
        borderColor: '#ffb422',
        borderWidth: 2,
        borderDash: [],
        borderDashOffset: 0.0,
        pointBackgroundColor: '#ffb422',
        pointBorderColor: 'rgba(255,255,255,0)',
        pointHoverBackgroundColor: '#ffb422',
        pointBorderWidth: 20,
        pointHoverRadius: 4,
        pointHoverBorderWidth: 15,
        pointRadius: 4,
        data: [80, 100, 70, 80, 120, 80],
      }]
    };


    if (updatedData) {
      data.labels = Object.keys(updatedData);
      data.datasets[0].data = Object.values(updatedData);
    }

    this.myChart2 = new Chart(this.ctx, {
      type: 'bar',
      data: data,
      options: gradientBarChartConfiguration
    });

  }

  setDisclaimerRead() {
    this.cookieService.set( 'disclaimerRead', 'yes' );
  }

  isDisclaimerNotRead() {
    return this.cookieService.get("disclaimerRead") != 'yes';
  }
}

export class Stats {
  todayVolume: number;
  allTimeVolume: number;
  averageTimeBetweetTransactions: number;
  allTimeFrom: string;
  last5DaysOdlVolume: Array<number>;
  topVolumes: Map<string, number>;
}
