import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";

import { FooterComponent } from "./footer/footer.component";
import {DateAgoPipe} from "../pipes/date-ago.pipe";
@NgModule({
  imports: [CommonModule, RouterModule, NgbModule],
  declarations: [FooterComponent],
  exports: [FooterComponent]
})
export class ComponentsModule {}
