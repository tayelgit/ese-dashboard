package org.technikum.ese.websocketdemo.controller.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.technikum.ese.websocketdemo.events.modul.DataUpdate;
import org.technikum.ese.websocketdemo.model.Data;
@CrossOrigin(origins = "*")
@Controller
public class DataController {
    private static Logger logger = LoggerFactory.getLogger("logger");

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void sendData(DataUpdate data) {
        logger.info(data.getMessage().getName());
        messagingTemplate.convertAndSend("/dashboard/public", data.getMessage());
    }

}
