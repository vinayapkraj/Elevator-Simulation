import { NgModule ,CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NoopAnimationsModule} from '@angular/platform-browser/animations';

import { CommonModule } from '@angular/common';

import {MatButtonModule, MatMenuModule} from '@angular/material';
import {MatSnackBarModule} from '@angular/material';

import { AppComponent } from './app.component';
import { ElevatorService } from './services/elevator.service';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    MatButtonModule, MatMenuModule , MatSnackBarModule,
    BrowserModule,
    BrowserAnimationsModule,
    NoopAnimationsModule,
    HttpModule,
    FormsModule,
    CommonModule
  ],
  providers: [ElevatorService],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }
