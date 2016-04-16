/**
 * 
 */
package org.ChatApplication.data.lightweightDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 * @author Devdatta
 *
 */
public final class LightweightDbManager {

	private static Logger logger = Logger.getLogger(LightweightDbManager.class);
	private static String dbPath = "";
	private Connection connection;

	static {
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Failed to load H2 driver", e);
		}
	}

	private boolean checkIfDbPresent() {
		return false;
	}

	private void insertToDatabase(String dbName, String message) {

	}

	private ArrayList<String> getAllMessages(String dbName) {
		return null;
	}

	private void createConnection(String fileName) throws SQLException {
		
		connection = DriverManager.getConnection("jdbc:h2:./"+fileName);

	}

	private void deleteDatabase(String dbName) {

	}

	// adds message to database represented by clientId
	public void queueMessage(String clientId, String message) {

	}

	public ArrayList<String> getAllPendingMessages(String clientId) {
		return getAllMessages(clientId + ".db");
	}

	public void clearMessages(String clientId) {
		deleteDatabase(clientId + ".db");
	}



	public static void main(String[] args) throws SQLException {
		LightweightDbManager dbManager = new LightweightDbManager();
		dbManager.createConnection("test1");
		System.out.println("Hello");
	}
}
