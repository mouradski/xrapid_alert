import {AfterViewInit, Component, NgZone} from "@angular/core";
import {HttpClient} from '@angular/common/http';
import * as am4core from "@amcharts/amcharts4/core";
import * as am4maps from "@amcharts/amcharts4/maps";
import am4geodata_worldLow from "@amcharts/amcharts4-geodata/worldLow";
import {TablesService} from "../../pages/tables/tables.service";
import {Queue} from 'queue-typescript';
import {DeviceDetectorService} from "ngx-device-detector";


@Component({
    selector: "odl-map",
    templateUrl: "map.component.html"
})
export class MapComponent implements AfterViewInit {

    mapChart: any;
    am4maps: any;
    cities: any;
    cityImages: any;
    lineSeries: any;
    realtime: boolean = false;
    count: number = 0;

    public mobile: boolean;

    queue: Queue<any> = new Queue<any>();

    corridors: Map<string, any> = new Map();
    currencies: Map<string, any> = new Map();

    addCity(coords, title): any {
        let city = this.cities.mapImages.create();
        city.latitude = coords.latitude;
        city.longitude = coords.longitude;
        city.tooltipText = title;
        return city;
    }

    addLine(from, to): any {
        let line = this.lineSeries.mapLines.create();
        line.imagesToConnect = [from, to];
        line.line.controlPointDistance = -0.4;

        return line;
    }


    private notifyOdl(source: string, destination: string, amount: number) {

        let corridor = source + "-" + destination;

        console.log("notification corridor : " + corridor);

        if (!this.corridors.has(corridor)) {
            this.corridors.set(corridor, this.addLine(this.currencies.get(source), this.currencies.get(destination)));
        } else {
            console.log("old corridor");
        }


        this.showOdl(this.corridors.get(corridor), amount);
    }

    ngAfterViewInit(): void {
        this.zone.runOutsideAngular(() => {
            this.mapChart = am4core.create("mapdiv", am4maps.MapChart);
            this.mapChart.maxZoomLevel = 1;
            this.mapChart.seriesContainer.draggable = false;
            this.mapChart.seriesContainer.resizable = false;
            this.mapChart.geodata = am4geodata_worldLow;
            this.mapChart.projection = new am4maps.projections.Miller();
            this.mapChart.homeZoomLevel = 0;
            this.mapChart.homeGeoPoint = {
                latitude: 30,
                longitude: 10
            };

            let polygonSeries = this.mapChart.series.push(new am4maps.MapPolygonSeries());
            polygonSeries.useGeodata = true;
            polygonSeries.mapPolygons.template.fill = this.mapChart.colors.getIndex(0).lighten(0.5);
            polygonSeries.mapPolygons.template.nonScalingStroke = true;
            polygonSeries.exclude = ["AQ"];

            this.cityImages = this.mapChart.series.push(new am4maps.MapImageSeries());
            this.cityImages.mapImages.template.nonScaling = false;

            let citiesTemplate = this.cityImages.mapImages.template;
            let city = citiesTemplate.createChild(am4core.Image);

            if (this.mobile) {
                city.width = 16;
                city.height = 16;
            } else {
                city.width = 28;
                city.height = 28;
            }

            city.fill = this.mapChart.colors.getIndex(0).brighten(-0.2);
            city.nonScaling = false;
            city.tooltipText = "{title}";
            city.horizontalCenter = "middle";
            city.verticalCenter = "middle";
            city.zIndex = 10;
            city.propertyFields.href = "flag";
            // Set property fields
            citiesTemplate.propertyFields.latitude = "latitude";
            citiesTemplate.propertyFields.longitude = "longitude";

            // Add data for the cities
            this.cityImages.data = [{
                "latitude": -35.2820,
                "longitude": 149.1286,
                "title": "Australia",
                "flag": "https://cdn.countryflags.com/thumbs/australia/flag-3d-round-500.png"
            }, {
                "latitude": 42.7392,
                "longitude": -85.9902,
                "title": "United-States",
                "flag": "https://cdn.countryflags.com/thumbs/united-states-of-america/flag-3d-round-500.png"
            }, {
                "latitude": 14.6043,
                "longitude": 120.9822,
                "title": "Philippines",
                "flag": "https://cdn.countryflags.com/thumbs/philippines/flag-3d-round-500.png"
            }, {
                "latitude": 23.6345,
                "longitude": -102.5527,
                "title": "Mexico",
                "flag": "https://cdn.countryflags.com/thumbs/mexico/flag-3d-round-500.png"
            }, {
                "latitude": 13.7367,
                "longitude": 100.5231,
                "title": "Thailand",
                "flag": "https://cdn.countryflags.com/thumbs/thailand/flag-3d-round-500.png"
            }, {
                "latitude": 37.5326,
                "longitude": 127.0246,
                "title": "Korea",
                "flag": "https://cdn.countryflags.com/thumbs/south-korea/flag-3d-round-500.png"
            }, {
                "latitude": -22.9035,
                "longitude": -43.2096,
                "title": "Brasil",
                "flag": "https://cdn.countryflags.com/thumbs/brazil/flag-3d-round-500.png"
            }, {
                "latitude": 47.5101,
                "longitude": 8.2055,
                "title": "Europe",
                "flag": "https://utility-scan.com/img/europe.png"
            }, {
                "latitude": 9.0777,
                "longitude": 8.67745,
                "title": "Nigeria",
                "flag": "https://cdn.countryflags.com/thumbs/nigeria/flag-3d-round-500.png"
            }, {
                "latitude": 55.5085,
                "longitude": -4.9257,
                "title": "United Kingdom",
                "flag": "https://cdn.countryflags.com/thumbs/united-kingdom/flag-3d-round-500.png"
            }, {
                "latitude": 35.6895,
                "longitude": 139.6917,
                "title": "Japan",
                "flag": "https://cdn.countryflags.com/thumbs/japan/flag-3d-round-500.png"
            }, {
                "latitude": -36.8484,
                "longitude": 174.7633,
                "title": "New Zealand",
                "flag": "https://cdn.countryflags.com/thumbs/new-zealand/flag-3d-round-500.png"
            }, {
                    "latitude": 39.92077,
                    "longitude": 32.85411,
                    "title": "Turkey",
                    "flag": "https://cdn.countryflags.com/thumbs/turkey/flag-3d-round-500.png"
            }, {
                "latitude": -28.48322,
                "longitude": 24.676997,
                "title": "South Africa",
                "flag": "https://cdn.countryflags.com/thumbs/south-africa/flag-3d-round-500.png"
            }
            ];

            this.cities = this.mapChart.series.push(new am4maps.MapImageSeries());
            this.cities.mapImages.template.nonScaling = true;

            this.currencies.set("USD", this.addCity({"latitude": 42.7392, "longitude": -85.9902}, "United-States"));
            this.currencies.set("PHP", this.addCity({"latitude": 14.6043, "longitude": 120.9822}, "Philippines"));
            this.currencies.set("MXN", this.addCity({"latitude": 23.6345, "longitude": -102.5527}, "Mexico"));
            this.currencies.set("AUD", this.addCity({"latitude": -35.2820, "longitude": 149.1286}, "Australia"));
            this.currencies.set("THB", this.addCity({"latitude": 13.7367, "longitude": 100.5231}, "Thailand"));
            this.currencies.set("KRW", this.addCity({"latitude": 37.5326, "longitude": 127.0246}, "Korea"));
            this.currencies.set("BRL", this.addCity({"latitude": -22.9035, "longitude": -43.2096}, "Brasil"));
            this.currencies.set("EUR", this.addCity({"latitude": 47.5101, "longitude": 8.2055}, "Europe"));
            this.currencies.set("GBP", this.addCity({"latitude": 55.5085, "longitude": -4.9257}, "United Kingdom"));
            this.currencies.set("JPY", this.addCity({"latitude": 35.6895, "longitude": 139.6917}, "Japan"));
            this.currencies.set("NGN", this.addCity({"latitude": 9.0777, "longitude": 8.67745}, "Nigeria"));
            this.currencies.set("NZD", this.addCity({"latitude": -36.8484, "longitude": 174.7633}, "New Zealand"));
            this.currencies.set("TRY", this.addCity({"latitude": 39.92077, "longitude": 32.85411}, "Turkey"));
            this.currencies.set("ZAR", this.addCity({"latitude": -28.48322, "longitude": 24.676997}, "South Africa"));

            this.lineSeries = this.mapChart.series.push(new am4maps.MapArcSeries());
            this.lineSeries.mapLines.template.line.strokeWidth = 3;
            this.lineSeries.mapLines.template.line.strokeOpacity = 0.5;
            this.lineSeries.mapLines.template.line.stroke = city.fill;
            this.lineSeries.mapLines.template.line.nonScalingStroke = true;
            this.lineSeries.mapLines.template.line.strokeDasharray = "1,1";
            this.lineSeries.zIndex = 0;
        });
    }

    goOdl(b, p) {
        let from = b.position, to;
        if (from == 0) {
            to = 1;
            p.rotation = 0;
        } else {
            to = 0;
            p.rotation = 180;
        }

        let animation = b.animate({
            from: from,
            to: to,
            property: "position"
        }, 5000, am4core.ease.sinInOut);

        animation.events.on("animationended", function () {
            p.dispose();
        });

    }

    showOdl(line, amount) {
        let bullet = line.lineObjects.create();
        bullet.nonScaling = true;
        bullet.position = 0;
        bullet.width = 48;
        bullet.height = 48;


        let odl = bullet.createChild(am4core.Sprite);

        if (this.mobile) {
            odl.scale = 0.005;
        } else {
            if (amount > 100000) {
                odl.scale = 0.01;

            } else if (amount > 50000) {
                odl.scale = 0.009;

            } else if (amount > 20000) {
                odl.scale = 0.008;

            } else if (amount > 5000) {
                odl.scale = 0.007;

            } else {
                odl.scale = 0.006;
            }
        }

        odl.horizontalCenter = "middle";
        odl.verticalCenter = "middle";
        odl.path = "M983 2240 c-265 -37 -485 -150 -669 -342 -262 -274 -366 -645 -284 -1019 44 -202 135 -371 285 -528 159 -168 350 -274 588 -328 99 -22 345 -22 444 0 238 54 429 160 588 328 91 95 135 157 191 266 83 163 118 313 118 508 0 195 -35 345 -118 508 -156 306 -439 521 -779 593 -88 18 -280 26 -364 14z m-196 -666 c203 -212 238 -236 339 -236 97 0 135 26 339 238 l177 184 99 0 c54 0 99 -3 99 -7 0 -8 -102 -115 -324 -341 -204 -207 -239 -227 -391 -227 -153 0 -188 20 -396 232 -225 229 -319 328 -319 336 0 4 45 7 99 7 l99 0 179 -186z m463 -520 c79 -25 130 -69 369 -316 l233 -243 -103 -3 -103 -3 -188 195 c-169 175 -194 197 -246 216 -42 16 -70 20 -104 16 -94 -10 -123 -32 -318 -234 l-184 -192 -104 0 -104 0 71 78 c39 42 151 159 248 258 142 144 190 187 235 209 93 44 197 51 298 19z";
        odl.fill = am4core.color("#000000");
        odl.strokeOpacity = 0;
        //odl.zIndex = 60;

        this.goOdl(bullet, odl);
    }

    constructor(private httpClient: HttpClient, private zone: NgZone, private tablesService: TablesService, private deviceService: DeviceDetectorService) {
        this.mobile = this.deviceService.isMobile();
        const _this = this;
        this.tablesService.getSingleData().subscribe(data => {

            _this.queue.enqueue(data);

        });

        this.tablesService.getData().subscribe(data => {

            for (let i = 0; i < data.length; i++) {

                if (Math.floor((new Date().getTime() - data[i].timestamp) / 1000) <= 3600 * 3) {
                    _this.queue.enqueue(data[i]);
                    console.log(i);
                }
            }

            let historyInterval = setInterval(() => {
                let data = _this.queue.removeTail();

                if (data) {
                    _this.notifyOdl(data.sourceFiat, data.destinationFiat, data.amount);
                } else {
                    clearInterval(historyInterval);
                    _this.realtime = true;
                    setInterval(() => {
                        let data = _this.queue.removeTail();
                        if (data) {
                            _this.notifyOdl(data.sourceFiat, data.destinationFiat, data.amount);
                        }
                    }, 5000);
                }

            }, 600);

        });


    }


    ngOnInit() {
    }

}


