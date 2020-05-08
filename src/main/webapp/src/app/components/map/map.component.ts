import {AfterViewInit, Component, NgZone } from "@angular/core";
import {HttpClient} from '@angular/common/http';
import * as am4core from "@amcharts/amcharts4/core";
import * as am4maps from "@amcharts/amcharts4/maps";
import am4geodata_worldLow from "@amcharts/amcharts4-geodata/worldLow";
import {TablesService} from "../../pages/tables/tables.service";
import { Queue } from 'queue-typescript';
import {Payment} from "../../pages/tables/tables.component";


@Component({
    selector: "odl-map",
    templateUrl: "map.component.html"
})
export class MapComponent implements AfterViewInit {

    mapChart: any;
    am4maps: any;
    cities: any;
    lineSeries: any;

    queue:Queue<any> = new Queue<any>();

    corridors:Map<string, any> = new Map();
    currencies:Map<string, any> = new Map();

    addCity(coords, title) : any {
        let city = this.cities.mapImages.create();
        city.latitude = coords.latitude;
        city.longitude = coords.longitude;
        city.tooltipText = title;
        return city;
    }

    addLine(from, to) : any {
        let line = this.lineSeries.mapLines.create();
        line.imagesToConnect = [from, to];
        line.line.controlPointDistance = -0.3;

        return line;
    }


    private notifyOdl(source: string, destination: string) {

        let corridor = source + "-" + destination;

        console.log("notification corridor : " + corridor);

        if (!this.corridors.has(corridor)) {
            this.corridors.set(corridor, this.addLine(this.currencies.get(source), this.currencies.get(destination)));
        } else {
            console.log("old corridor");
        }

       // console.log(this.addLine(this.currencies.get(source), this.currencies.get(destination)));


        this.showOdl(this.corridors.get(corridor));
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




            this.cities = this.mapChart.series.push(new am4maps.MapImageSeries());
            this.cities.mapImages.template.nonScaling = true;

            let city = this.cities.mapImages.template.createChild(am4core.Circle);
            city.radius = 10;
            city.fill = this.mapChart.colors.getIndex(0).brighten(-0.2);
            city.strokeWidth = 2;
            city.stroke = am4core.color("#28a745");


            this.currencies.set("USD", this.addCity({ "latitude": 42.7392, "longitude":-85.9902 }, "United-States"));
            this.currencies.set("PHP", this.addCity({"latitude": 14.6043,"longitude": 120.9822}, "Philippines"));
            this.currencies.set("MXN", this.addCity({ "latitude": 23.6345, "longitude":  -102.5527 }, "Mexico"));
            this.currencies.set("AUD", this.addCity({"latitude": -35.2820,"longitude": 149.1286}, "Australia"));
            this.currencies.set("THB", this.addCity({"latitude": 13.7367,"longitude": 100.5231},  "Thailand"));
            this.currencies.set("KRW", this.addCity({"latitude": 37.5326,"longitude": 127.0246},  "Korea"));
            this.currencies.set("BRL", this.addCity({"latitude": -22.9035,"longitude": -43.2096},  "Brasil"));

            this.lineSeries = this.mapChart.series.push(new am4maps.MapArcSeries());
            this.lineSeries.mapLines.template.line.strokeWidth = 3;
            this.lineSeries.mapLines.template.line.strokeOpacity = 0.5;
            this.lineSeries.mapLines.template.line.stroke = city.fill;
            this.lineSeries.mapLines.template.line.nonScalingStroke = true;
            this.lineSeries.mapLines.template.line.strokeDasharray = "1,1";
            this.lineSeries.zIndex = 10;
        });
    }

    goPlane(b,p) {
        let from = b.position, to;
        if (from == 0) {
            to = 1;
            p.rotation = 0;
        }
        else {
            to = 0;
            p.rotation = 180;
        }

        let animation = b.animate({
            from: from,
            to: to,
            property: "position"
        }, 5000, am4core.ease.sinInOut);

        animation.events.on("animationended", function(){
            p.dispose();
        });

    }

     showOdl(line) {
        let bullet = line.lineObjects.create();
        bullet.nonScaling = true;
        bullet.position = 0;
        bullet.width = 48;
        bullet.height = 48;

        let plane = bullet.createChild(am4core.Sprite);
        plane.scale = 0.01;
        plane.horizontalCenter = "middle";
        plane.verticalCenter = "middle";
        //plane.path = "m2,106h28l24,30h72l-44,-133h35l80,132h98c21,0 21,34 0,34l-98,0 -80,134h-35l43,-133h-71l-24,30h-28l15,-47";
        plane.path = "M1297.93 53.81c-131.41 77.2-209.22 216.61-209.22 363.62 0 77.2 31 155 69.81 224.41 31 62 46.2 170.21-62 224.41-77.21 46.2-178.22 15.4-224.42-62-46.21-62-100.41-124-170.22-170.21-131.41-77.2-286.43-77.2-417.85 0S75 851 75 998.05s77.21 286.41 209 363.82c131.41 77.2 286.43 77.2 417.85 0 69.81-38.8 124-100.4 162.42-170.21 31-54.2 116.21-124 224.42-62 77.21 46.2 100.41 147.21 62 224.41-38.8 69.8-62 147.21-62 224.41 0 147.21 77.21 286.41 209.22 363.62 131.41 77.2 286.43 77.2 417.85 0S1925 1725.49 1925 1578.48s-77.41-286.41-209.22-363.82c-69.81-38.8-147.22-54.2-232.23-54.2-69.81 0-162.42-46.2-162.42-162.41 0-93 69.81-162.41 162.42-162.41 77.21 0 162.42-15.4 232.23-54.2C1847.19 704.24 1925 564.83 1925 417.83s-77.41-286.41-209.22-363.62c-62-38.8-139.22-54.2-209-54.2-69.41-.4-147.22 15.4-208.82 53.8";
        plane.fill = am4core.color("#d70c0c");
        plane.strokeOpacity = 0;

        this.goPlane(bullet, plane);
    }

    constructor(private httpClient: HttpClient, private zone: NgZone, private tablesService: TablesService) {
        const _this = this;
        this.tablesService.getSingleData().subscribe(data => {

            //_this.notifyOdl(data.sourceFiat, data.destinationFiat);
            _this.queue.enqueue(data);

        });

        this.tablesService.getData().subscribe(data => {

            for (let i = 0; i < data.length && i < 5; i++) {
                console.log(i);
                _this.queue.enqueue(data[i]);
            }

        });

        setInterval(() => {
            let data = _this.queue.dequeue();

            if (data) {
                _this.notifyOdl(data.sourceFiat, data.destinationFiat);
            }
        }, 5000);
    }


    ngOnInit() {
    }

}


