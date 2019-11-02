import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { RouterModule } from "@angular/router";
import { ToastrModule } from 'ngx-toastr';

import { AppComponent } from "./app.component";
import { AdminLayoutComponent } from "./layouts/admin-layout/admin-layout.component";

import { NgbModule } from "@ng-bootstrap/ng-bootstrap";

import { AppRoutingModule } from "./app-routing.module";
import { ComponentsModule } from "./components/components.module";
import {AdminLayoutModule} from "./layouts/admin-layout/admin-layout.module";

import { CookieService } from 'ngx-cookie-service';
import {DateAgoPipe} from "./pipes/date-ago.pipe";

@NgModule({
  imports: [
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    ComponentsModule,
    NgbModule,
    RouterModule,
    AppRoutingModule,
    ToastrModule.forRoot(),
    AdminLayoutModule
  ],
  declarations: [AppComponent, AdminLayoutComponent, DateAgoPipe],
  providers: [CookieService, DateAgoPipe],
  bootstrap: [AppComponent]
})
export class AppModule {}
