package com.itson.WebsocketServer.config;


import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PresenceEventListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final Set<String> usuarios = ConcurrentHashMap.newKeySet();

    public  PresenceEventListener(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void onConnect(SessionConnectedEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String nombre = (String) accessor.getSessionAttributes().get("nombre");
        if (nombre != null){
            usuarios.add(nombre);
            messagingTemplate.convertAndSend("/tema/usuarios", usuarios);
        }
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String nombre = (String) accessor.getSessionAttributes().get("nombre");
        if (nombre != null){
            usuarios.remove(nombre);
            messagingTemplate.convertAndSend("/tema/usuarios");
        }
    }

}
