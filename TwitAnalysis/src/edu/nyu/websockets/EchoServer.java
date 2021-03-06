/*
 
 Cloud Computing
 Assignment 1
 
 Created by Abhishek Sharma on 10/27/15.
 Copyright (c) 2015 Abhishek. All rights reserved.
 
*/
package edu.nyu.websockets;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import edu.nyu.alchemy.Alchemy;
import edu.nyu.kafka.KafkaConsumer;
import edu.nyu.tweets.TweetsFetcher;

/**
 * @ServerEndpoint gives the relative name for the end point This will be
 *                 accessed via ws://localhost:8080/TwitAnalysis/echo Where
 *                 "localhost" is the address of the host, "EchoChamber" is the
 *                 name of the package and "echo" is the address to access this
 *                 class from the server
 */
@ServerEndpoint("/echo")
public class EchoServer {
	/**
	 * @OnOpen allows us to intercept the creation of a new session. The session
	 *         class allows us to send data to the user. In the method onOpen,
	 *         we'll let the user know that the handshake was successful.
	 */
	@OnOpen
	public void onOpen(Session session) {
		System.out.println(session.getId() + " has opened a connection");
		try {
			session.getBasicRemote().sendText("Connection Established");
			ExecutorService executor = Executors.newFixedThreadPool(1);
			executor.submit(new TweetsFetcher());
			KafkaConsumer kafkaConsumer = new KafkaConsumer("tweets");
			kafkaConsumer.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * When a user sends a message to the server, this method will intercept the
	 * message and allow us to react to it. For now the message is read as a
	 * String.
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("Message from " + session.getId() + ": " + message);

		try {
			String s = null;
			while ((s = Alchemy.queue.poll(100, TimeUnit.SECONDS)) != null) {
				System.out.println(s);
				session.getBasicRemote().sendText(s);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The user closes the connection.
	 * 
	 * Note: you can't send messages to the client from this method
	 */
	@OnClose
	public void onClose(Session session) {
		System.out.println("Session " + session.getId() + " has ended");
	}
}
