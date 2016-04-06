package org.ChatApplication.ui.service.utilities;

import java.io.IOException;

import org.ChatApplication.ui.service.application.ChatApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateGroup {
VBox groupContainer;


public void loadCreateGroupPage() throws IOException
{
groupContainer = (VBox) FXMLLoader.load(Login.class.getResource("/org/ChatApplication/ui/service/stylesheets/CreateGroupWindow.fxml"));	

HBox controlBox = (HBox) groupContainer.lookup("#controlBox");

VBox selectBox = (VBox) groupContainer.lookup("#selectBox");
VBox selectResultBox = (VBox) groupContainer.lookup("#selectResultBox");




Scene scene = new Scene(groupContainer, ChatApp.stage.getWidth(), ChatApp.stage.getHeight());
scene.getStylesheets().add(ChatApp.class.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());

ChatApp.stage.setScene(scene);
}

	
	
}
