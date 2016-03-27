package org.ChatApplication.server;

import java.io.IOException;

import org.ChatApplication.server.receiver.ServerModule;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		ServerModule serverModule = new ServerModule();
		try {
			serverModule.start(1515);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
