import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';

import * as SockJS from 'sockjs-client';
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Injectable()
export class DataServiceService {


  private data = new BehaviorSubject<Array<any>>([]);

  private stompClient;
  constructor() {
    this.init();
  }

  init(){
    let ws = new SockJS("http://localhost:8080/ws");
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, (frame) =>{
      that.stompClient.subscribe("/dashboard/public", (message) => {
        let dataObject = JSON.parse(message.body);
        let arrayValue = this.data.getValue();
        let index =  arrayValue.findIndex((pay)=>{
            return pay.name == dataObject.name;
          });

        console.log(index);
        if(index != -1){
          console.log("Adding data from module"+dataObject.name+"to dashboard");
            arrayValue[index]= dataObject;
        }else{
          console.log("Updating Data from module"+arrayValue[index]);
          arrayValue.push(dataObject);
        }

        that.data.next(arrayValue);
      })
    })
  }

  getData() {
    return this.data.asObservable();
  }
}
