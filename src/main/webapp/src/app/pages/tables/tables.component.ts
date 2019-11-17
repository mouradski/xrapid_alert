import {Component, OnInit} from "@angular/core";
import * as Stomp from 'stompjs';
import {Client} from 'stompjs';
import * as SockJS from 'sockjs-client';
import {HttpClient} from '@angular/common/http';
import { ViewportScroller } from '@angular/common';


@Component({
  selector: "app-tables",
  templateUrl: "tables.component.html"
})
export class TablesComponent implements OnInit {

  public datasets: Array<Payment> = [];
  public payment: Payment;
  public notif: Payment;

  public notifClass = '';


  public currentPage: Array<Payment>;
  public pageIndex: number;

  private client: Client;
  private recInterval = null;
  private socket = null;

  public notifier:string;

  constructor(private httpClient: HttpClient, readonly  viewportScroller: ViewportScroller) {
    const _this = this;
    this.payment = new Payment();
    this.pageIndex = 1;
    this.notifier = 'brad';

    httpClient.get<Payment[]>('/api/payments').subscribe(data => {

      for (let i = 0; i < data.length; i++) {
        _this.datasets.push(data[i]);
      }

      if (data.length > 0) {
        _this.payment = data[data.length - 1];
        _this.notifPayment(_this.payment);
      }

      _this.datasets =  _this.sort(_this.datasets);
      _this.currentPage = _this.paginate(this.datasets, 25, this.pageIndex);

      _this.newConnect();
    });


  }

  newConnect() {
    const _this = this;
    this.socket = new SockJS('/ws');
    this.client = Stomp.over(this.socket);

    this.client.connect({}, function (frame) {
      _this.client.subscribe('/topic/payments', function (message) {
        _this.datasets.push(JSON.parse(message.body));

        _this.payment = JSON.parse(message.body);

        if (_this.payment.amount > 2000) {
          _this.notifPayment(_this.payment);
        }

        _this.datasets =  _this.sort(_this.datasets);
        _this.currentPage = _this.paginate(_this.datasets, 25, _this.pageIndex)
      });
    });

    clearInterval(this.recInterval);

    this.socket.onclose = function () {
      _this.socket = null;
      _this.recInterval = setInterval(function() {
        _this.newConnect();
      }, 60000);
    }
  }

  sort(data: Array<Payment>) {
    return data.sort((a, b) => (a.timestamp > b.timestamp) ? -1 : 0);
  }

  ngOnInit() {
  }

  notifPayment(payment) {
    const _this = this;

    if (payment.amount > 20000) {
      _this.notif = payment;
      _this.notifClass = 'active';

      setTimeout(function () {
        //_this.notif = null;
        _this.notifClass = 'out';
      }, 5890);
    }
  }

  info(data) {
    let transactionHash = data[0][0];

    const _this = this;

    this.datasets.forEach(function (payment) {

      if (transactionHash == payment.transactionHash) {
        _this.payment = payment;
      }

    })
  }

  left() {
    if (this.pageIndex > 1) {
      this.pageIndex--;
      this.currentPage = this.paginate(this.datasets, 25, this.pageIndex);

      this.viewportScroller.scrollToPosition([0, 0]);
    }
  }

  right() {

    if (this.paginate(this.datasets, 25, (this.pageIndex + 1)).length > 1) {
      if ((this.pageIndex * 25) <= (this.datasets.length)) {
        this.pageIndex++;
        this.currentPage = this.paginate(this.datasets, 25, this.pageIndex);

        this.viewportScroller.scrollToPosition([0, 0]);
      }
    }
  }

  paginate(array: Array<Payment>, page_size, page_number) {
    --page_number;
    return array.slice(page_number * page_size, (page_number + 1) * page_size);
  }
}

export class Payment {
  dateTime: string;
  timestamp: number;
  transactionHash: string;
  amount: number;
  source: string;
  destination: string;
  destinationFiat: string;
  sourceFiat: string;
  tradeIds: string;
  tradeOutIds: string;
  usdValue: number;
  spottedAt: String;
  tag:number;
}
