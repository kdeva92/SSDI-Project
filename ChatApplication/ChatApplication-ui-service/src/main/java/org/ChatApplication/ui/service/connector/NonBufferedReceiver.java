/**
 * 
 */
package org.ChatApplication.ui.service.connector;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.ChatApplication.common.converter.ByteToEntityConverter;
import org.ChatApplication.common.converter.EntityToByteConverter;
import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.ui.service.NonBufferedReceiverTest;
import org.ChatApplication.ui.service.observer.MessageListener;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * @author Devdatta
 *
 */
public class NonBufferedReceiver {

	InputStreamReader streamreader;
	public MessageListener messageListener;
	public NonBufferedReceiver(Socket socket,MessageListener ml) throws IOException {
		// TODO Auto-generated constructor stub
		this.messageListener = ml;
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
					System.exit(1);
					continue;
				}
				if(read == Message.START_OF_MESSAGE){
					data = new byte[Message.MAX_MESSAGE_SIZE];
				}
				else if(read == Message.END_OF_MESSAGE){
					Message message = MessageUtility.getMessage(ByteBuffer.wrap(data));
					//uncomment below for test
					//NonBufferedReceiverTest.showReceivedMessage(message);	
					
					
							try {
								User recd_user = ByteToEntityConverter.getInstance().getUser(message.getData());
								messageListener.updateLogin(recd_user);
							} catch (JsonParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								try {
									messageListener.updateLogin(null);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}								
							} catch (JsonMappingException e) {
								// TODO Auto-generated catch block
								try {
									messageListener.updateLogin(null);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}								
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								try {
									messageListener.updateLogin(null);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}								
								e.printStackTrace();
							}
						
					
				}
				else{
					data[size++] = (byte)read; 
				}
				
				
			}
		}
		
	}
	
	
	
}
