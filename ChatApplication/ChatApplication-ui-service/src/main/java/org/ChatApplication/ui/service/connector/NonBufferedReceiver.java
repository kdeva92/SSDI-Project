/**
 * 
 */
package org.ChatApplication.ui.service.connector;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.ui.service.NonBufferedReceiverTest;

/**
 * @author Devdatta
 *
 */
public class NonBufferedReceiver {

	InputStreamReader streamreader;
	
	public NonBufferedReceiver(Socket socket) throws IOException {
		// TODO Auto-generated constructor stub
		streamreader = new InputStreamReader(socket.getInputStream());
		new Thread(new ReceiverPoller()).start();
	}
	
	class ReceiverPoller implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			byte[] data = new byte[Message.MAX_MESSAGE_SIZE];
			int size = 0;
			int read;
			while(true){
				
				try {
					read = streamreader.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				if(read == Message.START_OF_MESSAGE){
					data = new byte[Message.MAX_MESSAGE_SIZE];
				}
				else if(read == Message.END_OF_MESSAGE){
					Message message = MessageUtility.getMessage(ByteBuffer.wrap(data));
					//uncomment below for test
					NonBufferedReceiverTest.showReceivedMessage(message);					
				}
				else{
					data[size++] = (byte)read; 
				}
				
				
			}
		}
		
	}
	
	
	
}
