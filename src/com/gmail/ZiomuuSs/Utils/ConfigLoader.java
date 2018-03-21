package com.gmail.ZiomuuSs.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import com.gmail.ZiomuuSs.Main;
import com.gmail.ZiomuuSs.Nation.Group;
import com.gmail.ZiomuuSs.Nation.Nation;
import com.gmail.ZiomuuSs.Nation.NationMember;

public class ConfigLoader {
  private ConfigAccessor msgAccessor;
  private ConfigurationSection config;
  private Main plugin;
  private HashSet<Nation> nations = new HashSet<>(); //list of all nations
  //options variables
  public String outlawNation; //name of nation that is always default (if an estate is not belonging to any exiting nation, it belongs to this faction
  private Connection connection; //mysql connection
  private String host, database, username, password, prefix; //mysql data
  private int port; //mysql port
  
  ConfigLoader(Main plugin) {
    this.plugin = plugin;
    plugin.saveDefaultConfig();
    msgAccessor = new ConfigAccessor(plugin, "Messages.yml");
    msgAccessor.saveDefaultConfig();
    msg.set(msgAccessor.getConfig());
    config = plugin.getConfig();
    loadOptions();
    try {
      connectToMySQL();
    } catch (Exception e) {
      Bukkit.getLogger().log(Level.SEVERE, "[Nations] CANNOT CONNECT TO MYSQL DATABASE! SWITCHING OFF PLUGIN!", e);
      Bukkit.getPluginManager().disablePlugin(plugin);
    }
  }

  private void connectToMySQL() throws SQLException, ClassNotFoundException, NullPointerException{
      if (connection != null && !connection.isClosed()) {
          return;
      }
   
      synchronized (this) {
          Class.forName("com.mysql.jdbc.Driver");
          connection = DriverManager.getConnection("jdbc:mysql://" + host+ ":" + port + "/" + database, username, password);
      }
  }
  
  private void loadOptions() {
    outlawNation = config.getString("outlawNation");
    database = config.getString("mysql.database");
    host = config.getString("mysql.host");
    username = config.getString("mysql.username");
    password = config.getString("mysql.password");
    prefix = config.getString("mysql.prefix");
    port = config.getInt("mysql.port");
  }
  
  //methods connected with nations
  
  //loading nations
  private void loadNations() {
    
  }
  
  public void saveNation(Nation nation) {
    //todo
  }
  
  public void saveNationMember(NationMember player) {
    //todo
  }
  
  public void saveGroup(Group group) {
    //todo
  }
}
