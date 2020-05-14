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
import {BannerComponent} from "../../components/banner/banner.component";
import {BannerMobileComponent} from "../../components/banner-mobile/banner-mobile.component";
import {ValidatorComponent} from "../../components/validator/validator.component";
import {MapComponent} from "../../components/map/map.component";
import {NgxSpinnerModule} from "ngx-spinner";

@NgModule({
    imports: [
        CommonModule,
        RouterModule.forChild(AdminLayoutRoutes),
        FormsModule,
        HttpClientModule,
        NgbModule,
        NgxSpinnerModule
    ],
  exports: [
    TablesComponent
  ],
  declarations: [
    DashboardComponent,
    TablesComponent, BannerComponent, BannerMobileComponent, ValidatorComponent, MapComponent,
      DateAgoPipe
  ]
})
export class AdminLayoutModule {}
