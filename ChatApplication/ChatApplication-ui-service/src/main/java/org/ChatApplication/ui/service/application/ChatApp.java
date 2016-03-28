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
	
import org.ChatApplication.ui.service.utilities.Homepage;

import javafx.application.Application;
import javafx.stage.Stage;




public class ChatApp extends Application {
	public static Stage stage ;
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("UNCC Chat Application");
		stage = primaryStage;
		primaryStage.setHeight(700);
		primaryStage.setWidth(1000);
		
		//Loading Homepage
		Homepage homepage = new Homepage();
		homepage.loadHomepage();
		
		
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
