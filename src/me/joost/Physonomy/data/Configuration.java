package me.joost.Physonomy.data;

import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import me.joost.Physonomy.Physonomy;
import me.joost.Physonomy.data.type.SQLConnection;
import me.joost.Physonomy.data.type.YAMLConnection;

public class Configuration {
	
	private SQLConnection sql = null;
	
	public Configuration(Physonomy api) {
		api.saveDefaultConfig();
		
		FileConfiguration conf = api.getConfig();
		
		if(conf.getBoolean("mysql-enabled")) {
			
			sql = new SQLConnection(api, conf.getString("mysql-host"), conf.getString("mysql-port"), conf.getString("mysql-username"), conf.getString("mysql-password"), conf.getString("mysql-database"));
			try {
				if(sql.getConnection() != null && !sql.getConnection().isClosed()) {sql.loadAll();}else {
					sql = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		if(sql == null) {
			new YAMLConnection().loadAll();
		}
	}

	public static double MAX_DEBT_BANK; // If reached players will hold their money but the bank owner(s) will no longer have access to any funds
	public static boolean CONFIRM_CREDIT_TRANSACTION; // Player must confirm that they want to use credit when they don't have enough funds

	public static boolean MUST_ACCEPT_LOAN; // Player must confirm loan

	public static boolean INTEGRATE_TO_VAULT;
	
	public static String CURRENCY_NAME_SINGULAR;
	public static String CURRENCY_NAME_PLURAL;
	
	
	
}
