package org.ChatApplication.ui.service.observer;

import org.ChatApplication.server.message.Message;
import org.ChatApplication.ui.service.application.ChatApp;
import org.ChatApplication.ui.service.utilities.Presenter;

import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;

public class MessageListener {
	private Message message;
	public Presenter presenter;

	public MessageListener(Presenter present) {
		this.presenter = present;

	}

	public void startListening() {
		listenQueue();

	}

	private void listenQueue() {
		// TODO Auto-generated method stub
		System.out.println("iN listenqueue");
//		Task task = new Task<Void>() {
//
//			@Override
//			protected Void call() throws Exception {
//				// TODO Auto-generated method stub
//				return null;
//			}
//		};
//		ChatApp.messageQueue.addListener(new ListChangeListener<Message>() {
//			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Message> c) {
//				// TODO Auto-generated method stub
//				System.out.println(ChatApp.messageQueue.size());
//				
//				if (ChatApp.messageQueue != null && ChatApp.messageQueue.size() > 0) {
//					
//					presenter.handleUI(ChatApp.messageQueue.get(0));
//					ChatApp.messageQueue.remove(0);
//				}
//			}
//
//		});
		// Task<Void> task = new Task<Void>() {
		//
		// @Override
		// protected Void call() throws Exception {
		// // TODO Auto-generated method stub
		//
		// while (true) {
		// // if(ChatApp.messageQueue.isEmpty())
		// // continue;
		// System.out.println("iN cALL");
		// message = null;
		// message = ChatApp.messageQueue.poll();
		// if (message != null)
		// presenter.handleUI(message);
		// }
		// }
		// };
		// stateProperty for Task:
		// task.stateProperty().addListener(new ChangeListener<Worker.State>() {
		//
		// public void changed(ObservableValue<? extends State> observable,
		// State oldValue, Worker.State newState) {
		// if(newState==Worker.State.SUCCEEDED){
		// loadPanels(root);
		// }
		// }
		// });

	}

}
