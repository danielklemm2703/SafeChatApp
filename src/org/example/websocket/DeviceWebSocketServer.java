package org.example.websocket;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import phone.PhoneManager;

@ApplicationScoped
@ServerEndpoint("/actions")
public class DeviceWebSocketServer {

	@Inject
	private DeviceSessionHandler sessionHandler;

	@OnOpen
	public void open(Session session) {
		System.out.println("addSession");
		sessionHandler.addSession(session);
	}

	@OnClose
	public void close(Session session) {
		System.out.println("removeSession");
		sessionHandler.removeSession(session);
	}

	@OnError
	public void onError(Throwable error) {
		System.out.println("error");
		Logger.getLogger(DeviceWebSocketServer.class.getName()).log(
				Level.SEVERE, null, error);
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
		System.out.println("handleMessage");
		try (JsonReader reader = Json.createReader(new StringReader(message))) {
			JsonObject jsonMessage = reader.readObject();

			if ("registerPhoneNumber".equals(jsonMessage.getString("action"))) {
				PhoneManager.instance().registerPhoneNumber(jsonMessage.getString("phoneNumber"),session);
			}
		}
	}
}