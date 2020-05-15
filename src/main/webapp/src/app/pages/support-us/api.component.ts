import {Component, HostListener, OnInit} from "@angular/core";
import {HttpClient} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';
import {TablesService} from "../tables/tables.service";
import {NgxSpinnerService} from "ngx-spinner";
import * as SockJS from "sockjs-client";
import * as Stomp from 'stompjs';


@Component({
    selector: "app-api",
    templateUrl: "api.component.html"
})
export class ApiComponent implements OnInit {

    qrCodeUrl: string;
    paymentId: string;
    apiKey: ApiKey = null;
    days: Number = 30;
    renew: boolean;
    key:string = '';
    private recInterval = null;

    init: string = null;

    readonly socketCode: string = '' +
        '       let socket = new SockJS(\'https://api.utility-scan.com/websocket\');\n' +
        '         let client = Stomp.over(socket);\n' +
        '         client.connect({}, function () {\n' +
        '                client.subscribe(\'/top/odl\', function (odlPayment) {\n' +
        '                    console.log(odlPayment);\n' +
        '                }, {apiKey: "test"})\n' +
        '            }\n' +
        '         );'


    constructor(private httpClient: HttpClient, private cookieService: CookieService, private tablesService: TablesService, private spinner: NgxSpinnerService) {


    }

    requestKey() {
        console.log(this.days);
        this.spinner.show();

        let url  = this.renew ? '/api/xumm?days=' + this.days + '&key=' + this.key : '/api/xumm?days=' + this.days;

        this.httpClient.get<PaymentRequestInformation>(url).subscribe(data => {
            this.qrCodeUrl = data.qrCodeUrl;
            this.paymentId = data.paymentId;

            this.spinner.hide();

            this.recInterval = setInterval(() => {
                this.httpClient.get<ApiKey>('/api/xumm/' + data.paymentId).subscribe(apiKey => {
                    if (apiKey.key != "REJECTED" && apiKey.key != "WAITING") {
                        this.apiKey = apiKey;
                        clearInterval(this.recInterval);
                    } else if (apiKey.key == "REJECTED") {
                        this.apiKey = null;
                        this.qrCodeUrl = null;
                        clearInterval(this.recInterval);
                    } else {
                        //TODO
                    }
                })
            }, 2000);

        }, error => {
            console.log("errrrrr");
            console.log(error);
        });
    }

    ngOnInit() {
        this.gotoTop();
    }

    isShow: boolean;
    topPosToStartShowing = 100;

    @HostListener('window:scroll')
    checkScroll() {

        // window의 scroll top
        // Both window.pageYOffset and document.documentElement.scrollTop returns the same result in all the cases. window.pageYOffset is not supported below IE 9.

        const scrollPosition = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;

        console.log('[scroll]', scrollPosition);

        if (scrollPosition >= this.topPosToStartShowing) {
            this.isShow = true;
        } else {
            this.isShow = false;
        }
    }

    // TODO: Cross browsing
    gotoTop() {
        window.scroll({
            top: 0,
            left: 0,
            behavior: 'smooth'
        });
    }


}

export class PaymentRequestInformation {
    paymentId: string;
    qrCodeUrl: string;
}

export class ApiKey {
    expiration: string;
    key: string;
}
