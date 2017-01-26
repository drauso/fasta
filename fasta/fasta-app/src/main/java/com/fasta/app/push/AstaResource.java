package com.fasta.app.push;

import java.io.Serializable;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.primefaces.push.impl.JSONEncoder;

import com.fasta.app.beans.ChatUsers;

@PushEndpoint("/{room}/{user}")
@Singleton
public class AstaResource {

    @PathParam("room")
    private String room;

    @PathParam("user")
    private String username;

    @Inject
    private ServletContext ctx;

    @Inject
    private EventBus eventBus;
    
    @OnOpen
    public void onOpen() {
    	eventBus.publish(room + "/*", "%s has entered the room '%s'");
    }

    @OnClose
    public void onClose() {
        ChatUsers users = (ChatUsers) ctx.getAttribute("chatUsers");
        users.remove(username);
        eventBus.publish(room + "/*", "%s has left the room");
    }

    @OnMessage(encoders = {JSONEncoder.class })
    public Serializable onMessage(Serializable message) {
        return message;
    }
}
