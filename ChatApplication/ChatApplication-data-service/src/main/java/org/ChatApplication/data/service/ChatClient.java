package org.ChatApplication.data.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 
 * @author Komal
 *
 */
public class ChatClient {

	private static final int PortNumber = 9090;

	public ChatClient() {
		  Socket MyClient = null;
		    try {
				MyClient = new Socket("Machine name", PortNumber);
				 PrintStream output = null;
				    try {
				       output = new PrintStream(MyClient.getOutputStream());
				    }
				    catch (IOException e) {
				       System.out.println(e);
				    }
				    DataOutputStream input = null;
				    try {
				       input = new DataOutputStream(MyClient.getOutputStream());
				    }
				    catch (IOException e) {
				       System.out.println(e);
				    }
				    output.close();
			        input.close();
			       MyClient.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
	}
}
