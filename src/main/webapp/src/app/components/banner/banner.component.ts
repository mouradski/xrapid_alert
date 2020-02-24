import { Component, AfterViewInit, Input } from "@angular/core"
@Component({
    moduleId: module.id,
    selector: "google-adsense",
    templateUrl: "banner.component.html"

})

export class BannerComponent implements AfterViewInit {

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
                //console.error(e);
            }
        }, this.delay);
    } }
