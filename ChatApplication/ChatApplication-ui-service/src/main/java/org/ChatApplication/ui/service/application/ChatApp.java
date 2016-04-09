/*
 * 
 * Author: Gaurav Dhamdhere
 * Last Modified on: 03/08/16
 * 
 * Description: Main File of the application.
 * Loads the Homepage when application is started.
 * 
 */

package org.ChatApplication.ui.service.application;
	
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ChatApplication.server.message.Message;
import org.ChatApplication.ui.service.utilities.Homepage;
import org.ChatApplication.ui.service.utilities.Presenter;

import javafx.application.Application;
import javafx.stage.Stage;




public class ChatApp extends Application {
	public static Stage stage ;
	public static ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();
	@Override
	public void start(Stage primaryStage) throws UnknownHostException, IOException {
		primaryStage.setTitle("UNCC Chat Application");
		stage = primaryStage;
		primaryStage.setHeight(700);
		primaryStage.setWidth(1000);
		
		//Loading Homepage
		Presenter presenter =null;
		Homepage homepage = new Homepage();
		presenter = new Presenter(homepage);
		homepage.loadHomepage(presenter);
		
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
}
