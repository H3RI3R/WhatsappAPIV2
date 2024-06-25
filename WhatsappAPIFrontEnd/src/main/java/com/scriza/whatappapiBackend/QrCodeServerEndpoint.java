package com.scriza.whatappapiBackend;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@ServerEndpoint(value = "/qr")
public class QrCodeServerEndpoint {

    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());
    private static Logger logger = Logger.getLogger(QrCodeServerEndpoint.class.getName());

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
        logger.info("QR Client connected: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        logger.info("QR Client disconnected: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("QR Message from client: " + message);
    }

    public static void notifyClients() {
        synchronized (clients) {
            for (Session client : clients) {
                try {
                    client.getBasicRemote().sendText("reload");
                    logger.info("Sent reload message to client: " + client.getId());
                } catch (IOException e) {
                    logger.severe("Failed to send reload message to client: " + e.getMessage());
                }
            }
        }
    }
}