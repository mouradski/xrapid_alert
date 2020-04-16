import {Component, OnInit} from "@angular/core";

@Component({
    selector: "app-support-layout",
    templateUrl: "./api-layout.component.html",
    styleUrls: ["./api-layout.component.scss"]
})
export class ApiLayoutComponent implements OnInit {
    public sidebarColor: string = "red";

    constructor() {}
    ngOnInit() {}
}
