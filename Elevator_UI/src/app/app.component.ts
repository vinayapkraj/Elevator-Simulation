import { Component } from '@angular/core';
import { ElevatorService } from './services/elevator.service';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  noOfElevators: number[] = [1, 2, 3, 4, 5, 6];
  noOfFloors: number[]= [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  title = 'app';
  selElevator: number;
  selFloor: number;
  responseRec: any;
  errorMsg: any;
  tofloor: number;

  public constructor(private elevatorService: ElevatorService,public snackBar: MatSnackBar) {
  }
  requestInsideElevator() {
      console.log('Selected Elevator ' + this.selElevator + ' ' + this.selFloor);
      if (this.selElevator == null || this.selFloor == null) {
        alert('Select Elevator / Floor value cannot be blank');
      }else {
      this.elevatorService.requestInsideElevators(this.selElevator, this.selFloor) .subscribe(
        (data) => {this.responseRec =  data ,  alert(data); },
        error => this. errorMsg = error );

        this.selElevator = null;
        this.selFloor = null ;
        this.openSnackBar('Inside Elevator Request Posted', 'OK');
      }
  }

  requestElevator(direction: string) {
      if (this.tofloor == null) {
        alert('Select Floor value cannot be blank');
      } else {
        this.elevatorService.requestElevators(this.tofloor, direction) .subscribe(
        (data) => {this.responseRec =  data ,  alert(data); },
        error => this. errorMsg = error );

        this.tofloor = null;
	      this.openSnackBar('Request Posted','OK');
        }
    }

	openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 2000,
    });
  }
}
