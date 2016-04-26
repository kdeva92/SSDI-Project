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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Platform;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.ui.service.database.DatabaseConnecter;
import org.ChatApplication.ui.service.utilities.Homepage;
import org.ChatApplication.ui.service.utilities.Presenter;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChatApp extends Application {
	public static Stage stage;
	// public static ConcurrentLinkedQueue<Message> messageQueue = new
	// ConcurrentLinkedQueue<Message>();
	public static ObservableList<Message> messageQueue = FXCollections.observableArrayList();
	private Presenter presenter;

	@Override
	public void start(Stage primaryStage) throws UnknownHostException, IOException, SQLException {
		primaryStage.setTitle("UNCC Chat Application");
		stage = primaryStage;
		primaryStage.setHeight(700);
		primaryStage.setWidth(1000);
		primaryStage.show();
		// Loading Homepage
		Homepage homepage = new Homepage();
		presenter = new Presenter(homepage);
		homepage.loadHomepage(presenter);
		DatabaseConnecter d = new DatabaseConnecter();
		Connection conn = d.getConn();

		Statement st = conn.createStatement();
//		ResultSet rs = st.executeQuery("select * from Groups_000000000");
//		while(rs.next()){
//			System.out.println(rs.getString(2));
//		}
//				 st.execute("INSERT INTO User VALUES('000000000','Gaurav','g@uncc.edu','9999999999','abcd')");
//				 st.execute("INSERT INTO User VALUES('000000002','Devd','g@uncc.edu','9999999999','abcd')");
//				 st.execute("INSERT INTO User VALUES('800934993','Komal','g@uncc.edu','9999999999','abcd')");
				// st.execute("INSERT INTO User
				// VALUES('800934994','Hiten','g@uncc.edu','9999999999','abcd')");
				// st.execute("INSERT INTO User VALUES('800934995','Honey
				// Singh','g@uncc.edu','9999999999','abcd')");

		// User u = new User();
		// u.setFirstName("Gaurav");
		// u.setLastName("D");
		// u.setEmail("g@uncc.edu");
		// u.setId(24);
		// u.setNinerId("800934991");
		// u.setPassword("ABCD");
		// ChatPage cp = new ChatPage();
		// cp.loadChatPage(null, u);

		ChatApp.messageQueue.addListener(new ListChangeListener<Message>() {
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Message> c) {
				// TODO Auto-generated method stub
				System.out.println(ChatApp.messageQueue.size());
				Platform.runLater(new Runnable() {

					public void run() {
						if (ChatApp.messageQueue != null && ChatApp.messageQueue.size() > 0) {
							System.out.println("Queue size "+ChatApp.messageQueue.size());
							Message msg = ChatApp.messageQueue.get(0);
							//presenter.handleUI(ChatApp.messageQueue.get(0));
							ChatApp.messageQueue.remove(0);
							presenter.handleUI(msg);
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
