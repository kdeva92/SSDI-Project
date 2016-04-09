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
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChatApp extends Application {
	public static Stage stage;
	// public static ConcurrentLinkedQueue<Message> messageQueue = new
	// ConcurrentLinkedQueue<Message>();
	public static ObservableList<Message> messageQueue = FXCollections.observableArrayList();
	private Presenter presenter;

	@Override
	public void start(Stage primaryStage) throws UnknownHostException, IOException {
		primaryStage.setTitle("UNCC Chat Application");
		stage = primaryStage;
		primaryStage.setHeight(700);
		primaryStage.setWidth(1000);
		primaryStage.show();
		// Loading Homepage
		Homepage homepage = new Homepage();
		presenter = new Presenter(homepage);
		homepage.loadHomepage(presenter);

		ChatApp.messageQueue.addListener(new ListChangeListener<Message>() {
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Message> c) {
				// TODO Auto-generated method stub
				System.out.println(ChatApp.messageQueue.size());
				Platform.runLater(new Runnable() {

					public void run() {
						if (ChatApp.messageQueue != null && ChatApp.messageQueue.size() > 0) {

							presenter.handleUI(ChatApp.messageQueue.get(0));
							ChatApp.messageQueue.remove(0);
						}

					}
				});

			}

		});
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			public void handle(WindowEvent event) {
				System.exit(0);

			}
		});

	}

	public static void main(String[] args) {
		launch(args);
	}

}
