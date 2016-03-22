package org.ChatApplication.data.service;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Komal
 *
 */
public class ChatServer {

	private static final int PortNumber = 9090;

	public ChatServer() {
		ServerSocket myService = null;
		try {
			myService = new ServerSocket(PortNumber);
		} catch (IOException e) {
			System.out.println(e);
		}
		Socket clientSocket = null;
		Socket serviceSocket = null;
		try {
			serviceSocket = myService.accept();
			DataInputStream input = null;
			try {
				input = new DataInputStream(serviceSocket.getInputStream());
			} catch (IOException e) {
				System.out.println(e);
			}
			PrintStream output = null;
			try {
				output = new PrintStream(serviceSocket.getOutputStream());
			} catch (IOException e) {
				System.out.println(e);
			}
			output.close();
			input.close();
			serviceSocket.close();
			myService.close();
		} catch (IOException e) {
			System.out.println(e);
		}

	}

}
