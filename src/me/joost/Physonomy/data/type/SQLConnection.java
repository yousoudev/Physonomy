package me.joost.Physonomy.data.type;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import me.joost.Physonomy.Physonomy;

public class SQLConnection {

	private Connection conn = null;
	
	public SQLConnection(Physonomy api, String host, String port, String username, String password, String database) {	
		api.getLogger().info("Trying to establish SQL connection");
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
		    conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
		    api.getLogger().info("Sucessfully established SQL connection");
		    
		    Bukkit.getScheduler().scheduleSyncRepeatingTask(api, new Runnable() {
		    	public void run() {
		    		refresh();
		    	}
		    }, 144000L, 144000L); // Refresh every 2h to make sure connection doesn't die
		    
		} catch (ClassNotFoundException | SQLException e) {
			
			api.getLogger().info("Failed to establish SQL connection, check your configuration");
			e.printStackTrace();
			
		}
	}
	
	public void loadAll() {
		
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	private void refresh() {
		try {
			
			PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM Dual");
			ResultSet set = ps.executeQuery();
			set.next();
			set.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			
		}
	}
	
	public PreparedStatement prepare(String query) throws SQLException {
		return conn.prepareStatement(query);
	}
	
	public void closeConnection() {
		if(conn != null) {
			try {
				if(!conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				
				e.printStackTrace();
				
			}
		}
	}
}
