package org.ChatApplication.ui.service.observer;

import java.io.IOException;

import org.ChatApplication.data.entity.User;
import org.ChatApplication.ui.service.models.Message;
import org.ChatApplication.ui.service.utilities.ChatPage;
import org.ChatApplication.ui.service.utilities.Presenter;

public class MessageListener {
	
	public Presenter presenter;
	public MessageListener(Presenter present)
	{
		this.presenter = present;
	}
	
	public void updateLogin(User user) throws IOException
	{
		this.presenter.handleLogin(user);
		
	}

}
