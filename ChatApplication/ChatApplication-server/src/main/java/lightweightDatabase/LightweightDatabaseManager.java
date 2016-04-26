package lightweightDatabase;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.ChatApplication.server.receiver.ServerModule;
import org.apache.log4j.Logger;

public class LightweightDatabaseManager {

	private final static Logger logger = Logger.getLogger(LightweightDatabaseManager.class);

	static {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.fatal("Cannot H2 load database driver", e);
		}
	}

	public static void storeMessage(String receiver, ByteBuffer message) throws SQLException {
		Connection cn = getDataBase(receiver);
		// String insertQuery = "INSERT INTO MESSAGE" + " values" + "('" + new
		// String(message.array()) + "')";
		// int res = cn.createStatement().executeUpdate(insertQuery);//
		// prepareStatement(insertQuery).executeUpdate();

		String insertQuery = "INSERT INTO MESSAGE" + " values(?)";
		PreparedStatement pst = cn.prepareStatement(insertQuery);
		pst.setBinaryStream(1, new ByteArrayInputStream(message.array()));

		int res = pst.executeUpdate();

		cn.commit();
		cn.close();
	}

	public static List<ByteBuffer> getAllMessagesForUser(String ninerId) throws SQLException, IOException {
		if (!checkIfDBPresent(ninerId))
			return null;
		Connection cn = getDataBase(ninerId);
		String selectQuery = "select * from message";
		ResultSet rs = cn.createStatement().executeQuery(selectQuery);
		ArrayList<ByteBuffer> list = new ArrayList<ByteBuffer>();
		while (rs.next()) {
			InputStream ipstream = rs.getBinaryStream(1);
			byte[] data = new byte[1400];
			ipstream.read(data);
			ByteBuffer buff = ByteBuffer.wrap(data);
			list.add(buff);
		}
		cn.close();
		File dbFile = new File("Databases" + File.separator + ninerId + ".mv.db");
		dbFile.delete();
		return list;
	}

	private static boolean checkIfDBPresent(String ninerId) {
		String fname = "Databases" + File.separator + ninerId + ".mv.db";
		File f = new File(fname);
		return f.exists();
	}

	private static Connection getDataBase(String ninerId) throws SQLException {

		if (checkIfDBPresent(ninerId)) {
			return openDatabase(ninerId);
		}
		return createDatabase(ninerId);
	}

	private static Connection openDatabase(String ninerId) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:h2:./Databases/" + ninerId, "", "");
		conn.setAutoCommit(true);
		return conn;
	}

	private static Connection createDatabase(String ninerId) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:h2:./Databases/" + ninerId, "", "");
		Statement st = conn.createStatement();
		st.execute("CREATE TABLE IF NOT EXISTS MESSAGE(message BINARY(1400))");
		// "CREATE TABLE IF NOT EXISTS MESSAGE(niner_id varchar(10),studentName
		// varchar(60),email varchar(60),contact varchar(10),password
		// varchar(20))");

		// String query = "CREATE TABLE MESSAGE(message varchar(1400))";
		// PreparedStatement createPreparedStatement =
		// conn.prepareStatement(query);
		// createPreparedStatement.executeUpdate();
		// createPreparedStatement.close();
		// createPreparedStatement.close();
		conn.commit();
		st.close();
		conn.setAutoCommit(true);
		return conn;
	}

	public static void main(String[] args) {
		try {
			ByteBuffer messageI1 = MessageUtility.packMessage("hello".getBytes(), "000000000", "000000001",
					ReceiverTypeEnum.INDIVIDUAL_MSG, MessageTypeEnum.CHAT_MSG, 1, 1);
			LightweightDatabaseManager.storeMessage("000000001", messageI1);

			List<ByteBuffer> retMessages = LightweightDatabaseManager.getAllMessagesForUser("000000001");
			ByteBuffer message2 = retMessages.get(0);
			System.out.println(new String(messageI1.array()).trim());
			System.out.println(new String(message2.array()).trim());
			System.out.println("Before flip:");
			for (int i = 0; i < 30; i++) {
				System.out.print(" " + message2.get(i));
			}
			message2.flip();
//			System.out.println("\nafter flip:");
//			for (int i = 0; i < 30; i++) {
//				System.out.print(" " + message2.get(i));
//			}

		} catch (BufferUnderflowException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
