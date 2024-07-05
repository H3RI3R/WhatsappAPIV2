package com.scriza.whatappapiBackend;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;


@ServerEndpoint("/sendMessages/{username}")
public class SendIndividualMessagesEndPoint {

    private static final Set<Session> sessions = Collections.newSetFromMap(new ConcurrentHashMap<Session, Boolean>());

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessions.add(session);
        session.getUserProperties().put("username", username);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Handle incoming message
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    public static void sendMessageToClient(String username, String message) {
        for (Session session : sessions) {
            if (session.getUserProperties().get("username").equals(username)) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
