package com.scriza.whatappapiBackend;


import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/qrCode")
public class QrCodeServerEndpoint {
    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
    }

    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        // Handle incoming messages if needed
    }

    public static void notifyClients() {
        clients.forEach(session -> {
            try {
                session.getBasicRemote().sendText("QR_UPDATED");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void notifyLoginSuccess() {
        clients.forEach(session -> {
            try {
                session.getBasicRemote().sendText("LOGIN_SUCCESS");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
