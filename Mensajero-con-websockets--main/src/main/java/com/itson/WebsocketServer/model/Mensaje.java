package com.itson.WebsocketServer.model;

public record Mensaje(
        String nombre,
        String contenido,
        String destinatario,
        String tipo) {

}