package org.technikum.ese.websocketdemo.events.modul;

import org.springframework.context.ApplicationEvent;
import org.technikum.ese.websocketdemo.model.Data;

public class DataUpdate extends ApplicationEvent {
    private Data message;

    public DataUpdate(Object source, Data message) {
        super(source);
        this.message = message;
    }

    public Data getMessage() {
        return message;
    }
}