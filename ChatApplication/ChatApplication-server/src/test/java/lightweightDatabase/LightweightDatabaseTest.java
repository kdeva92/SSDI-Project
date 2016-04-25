package lightweightDatabase;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;

import junit.framework.TestCase;

public class LightweightDatabaseTest extends TestCase {

	public void testDatabase() {

		try {
			ByteBuffer messageI1 = MessageUtility.packMessage("hello".getBytes(), "000000000", "000000001",
					ReceiverTypeEnum.INDIVIDUAL_MSG, MessageTypeEnum.CHAT_MSG, 1, 1);
			LightweightDatabaseManager.storeMessage("000000001", messageI1);
			
			ByteBuffer messageI2 = MessageUtility.packMessage("u there?".getBytes(), "000000000", "000000001",
					ReceiverTypeEnum.INDIVIDUAL_MSG, MessageTypeEnum.CHAT_MSG, 1, 1);
			LightweightDatabaseManager.storeMessage("000000001", messageI2);
			
			ByteBuffer messageI3 = MessageUtility.packMessage("Hello World!!".getBytes(), "000000000", "000000001",
					ReceiverTypeEnum.INDIVIDUAL_MSG, MessageTypeEnum.CHAT_MSG, 1, 1);
			LightweightDatabaseManager.storeMessage("000000001", messageI3);
			
			List<ByteBuffer> retMessages = LightweightDatabaseManager.getAllMessagesForUser("000000001");
			ByteBuffer message2 = retMessages.get(0);
			System.out.println(new String(messageI1.array()).trim());
			System.out.println(new String(message2.array()).trim());
			assertEquals(new String(messageI1.array()).trim(), new String(message2.array()).trim());
			
			message2 = retMessages.get(1);
			System.out.println(new String(messageI2.array()));
			System.out.println(new String(message2.array()));
			assertEquals(new String(messageI2.array()), new String(message2.array()));
			
			message2 = retMessages.get(2);
			System.out.println(new String(messageI3.array()).trim());
			System.out.println(new String(message2.array()).trim());
			assertEquals(new String(messageI3.array()).trim(), new String(message2.array()).trim());
			
		} catch (BufferUnderflowException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (SQLException e) {
			e.printStackTrace();
			assertTrue(false);
		} catch (IOException e) {
			e.printStackTrace();
			assertTrue(false);
		}

	}
	
}
