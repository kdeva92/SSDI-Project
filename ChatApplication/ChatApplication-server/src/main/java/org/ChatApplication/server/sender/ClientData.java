/**
 * 
 */
package org.ChatApplication.server.sender;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author Devdatta
 *
 */
public class ClientData {
	SocketChannel socketChannel;
	SelectionKey selectionKey;

	ClientData(SocketChannel socketChannel, SelectionKey selectionKey) {
		this.selectionKey = selectionKey;
		this.socketChannel = socketChannel;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}

	public SelectionKey getSelectionKey() {
		return selectionKey;
	}

	public void setSelectionKey(SelectionKey selectionKey) {
		this.selectionKey = selectionKey;
	}
}
