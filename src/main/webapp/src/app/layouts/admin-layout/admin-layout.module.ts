import {NgModule} from "@angular/core";
import {HttpClientModule} from "@angular/common/http";
import {RouterModule} from "@angular/router";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";

import {AdminLayoutRoutes} from "./admin-layout.routing";
import {DashboardComponent} from "../../pages/dashboard/dashboard.component";
import {TablesComponent} from "../../pages/tables/tables.component";

import {NgbModule} from "@ng-bootstrap/ng-bootstrap";
import {DateAgoPipe} from "../../pipes/date-ago.pipe";

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(AdminLayoutRoutes),
    FormsModule,
    HttpClientModule,
    NgbModule,
  ],
  exports: [
    TablesComponent
  ],
  declarations: [
    DashboardComponent,
    TablesComponent, DateAgoPipe
  ]
})
export class AdminLayoutModule {}
