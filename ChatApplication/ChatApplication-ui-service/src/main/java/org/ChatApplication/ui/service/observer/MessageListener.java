package org.ChatApplication.ui.service.observer;

import java.io.IOException;

import org.ChatApplication.common.converter.ByteToEntityConverter;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.ui.service.utilities.ChatPage;
import org.ChatApplication.ui.service.utilities.Presenter;

public class MessageListener {
	
	public Presenter presenter;
	public MessageListener(Presenter present)
	{
		this.presenter = present;
	}
	
	public void updateUI(Message message) throws IOException
	{
		if(message.getType().equals(MessageTypeEnum.LOG_IN_MSG))
		{
			this.presenter.handleLogin(message);
		}
	}

}
