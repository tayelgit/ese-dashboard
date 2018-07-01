import { Component } from '@angular/core';
import {DataServiceService} from './data-service.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
   dataArray = [];
  constructor(private dataService: DataServiceService) {
     this.dataService.getData().subscribe((data)=>{
       this.dataArray = data;
     });
  }

}
