package com.itson.WebsocketServer.controller;

import com.itson.WebsocketServer.model.Mensaje;
import com.itson.WebsocketServer.model.Producto;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MensajeController {

    private  final SimpMessagingTemplate messagingTemplate;

    public MensajeController(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/envio")
    public void envio(Mensaje mensaje, Principal principal){
        if (mensaje.destinatario() == null || mensaje.destinatario().equals("todos")){
            messagingTemplate.convertAndSend("/tema/mensajes", mensaje);
        }else{
            messagingTemplate.convertAndSendToUser(
                    mensaje.destinatario(), "/queue/privado", mensaje
            );
        }
    }

    @MessageMapping("/producto")
    public void producto(Producto producto, @Header("destinatario")String destinatario){
        if (destinatario == null || destinatario.equals("todos")){
            messagingTemplate.convertAndSend("/tema/productos", producto);
        }else {
            messagingTemplate.convertAndSendToUser(
                    destinatario, "/queue/producto", producto
            );
        }
    }
}
