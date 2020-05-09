import {NgModule} from "@angular/core";
import {HttpClientModule} from "@angular/common/http";
import {RouterModule} from "@angular/router";
import {ToastrModule} from 'ngx-toastr';

import {AppComponent} from "./app.component";
import {AdminLayoutComponent} from "./layouts/admin-layout/admin-layout.component";

import {NgbModule} from "@ng-bootstrap/ng-bootstrap";

import {AppRoutingModule} from "./app-routing.module";
import {ComponentsModule} from "./components/components.module";
import {AdminLayoutModule} from "./layouts/admin-layout/admin-layout.module";

import {CookieService} from 'ngx-cookie-service';
import {DeviceDetectorModule, DeviceDetectorService} from 'ngx-device-detector';
import {TablesService} from "./pages/tables/tables.service";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {FormsModule} from "@angular/forms";
import {ApiLayoutModule} from "./layouts/api-layout/api-layout.module";
import {ApiLayoutComponent} from "./layouts/api-layout/api-layout.component";

@NgModule({
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ComponentsModule,
    NgbModule,
    RouterModule,
    AppRoutingModule,
    DeviceDetectorModule,
    ToastrModule.forRoot(),
    AdminLayoutModule,
    ApiLayoutModule
  ],
  declarations: [AppComponent, AdminLayoutComponent, ApiLayoutComponent],
  providers: [CookieService, DeviceDetectorService, TablesService],
  bootstrap: [AppComponent]
})
export class AppModule {}
