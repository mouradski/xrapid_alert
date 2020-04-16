import {AfterViewInit, Component, Input} from "@angular/core"

@Component({
    moduleId: module.id,
    selector: "google-mobile-adsense",
    templateUrl: "banner-mobile.component.html"

})

export class BannerMobileComponent implements AfterViewInit {

    @Input() slotId : string;
    @Input() delay: number;
    @Input() style: string;
    constructor() {
    }

    ngAfterViewInit() {
        setTimeout(() => {
            try {
                (window["adsbygoogle"] = window["adsbygoogle"] || []).push({});
            } catch (e) {
                console.error(e);
            }
        }, this.delay);
    } }
