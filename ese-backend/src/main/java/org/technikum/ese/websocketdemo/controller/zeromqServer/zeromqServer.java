package org.technikum.ese.websocketdemo.controller.zeromqServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.technikum.ese.websocketdemo.WebsocketDemoApplication;
import org.technikum.ese.websocketdemo.events.modul.DataUpdate;
import org.technikum.ese.websocketdemo.model.Data;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import javax.annotation.PostConstruct;
import java.io.IOException;
@Component
public class zeromqServer extends Thread {


    private static Logger logger = LoggerFactory.getLogger("logger");



    @Autowired
    private ApplicationEventPublisher publisher;

    private ZContext context = new ZContext();
    @PostConstruct
    private void init(){

        this.start();
    }

    @Override
    public void run(){
        ZMQ.Socket clients = context.createSocket(ZMQ.ROUTER);
        clients.bind("tcp://*:5555");
        logger.info("Client bound to tcp://*:5555");

        ZMQ.Socket workers = context.createSocket(ZMQ.DEALER);
        workers.bind("inproc://workers");

        for (int thread_nbr = 0; thread_nbr < 5; thread_nbr++) {

            //publisher.publishEvent(new Data("bla","ba","bla"));

            logger.info("Worker Started");
             new Worker(context,publisher).start();
        }

        //  Connect work threads to client threads via a queue
        ZMQ.proxy(clients, workers, null);
    }

    private class Worker extends Thread implements ApplicationEventPublisherAware
    {

        private Worker(ZContext context, ApplicationEventPublisher publisher)
        {
            this.context = context;
            this.publisher= publisher;
        }
        private ZContext context ;
        private ApplicationEventPublisher publisher;


        @Override
        public void run()
        {
            ObjectMapper om = new ObjectMapper();
            ZMQ.Socket socket = context.createSocket(ZMQ.REP);
            socket.connect("inproc://workers");

            while (true) {


                String request = socket.recvStr(0);

                try {
                    logger.info("Request incomming");
                    Data data = om.readValue(request, Data.class);

                    DataUpdate dataUpdateEvent = new DataUpdate(this, data);
                    publisher.publishEvent(dataUpdateEvent);
                    logger.info(Thread.currentThread().getName() + " Received request: [" + request + "]");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }

        @Override
        public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
            this.publisher = applicationEventPublisher;
        }
    }

}
