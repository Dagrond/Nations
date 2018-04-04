package com.github.Dagrond.Utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class msg {
  
  private static ConfigurationSection msg;
  
  public static void set(ConfigurationSection msgConfig) {
    msg = msgConfig;
  }
  
  public static String get (String path, boolean prefix, String...opt) {
    if (!msg.isString(path))
      return ChatColor.RED+"Error occured during loading message. Perhaps Messages.yml is configured badly. Please, contact server operator.";
    String string = "";
    if (prefix)
      string = msg.getString("prefix");
    if (path.startsWith("error_")) {
      string += msg.getString("error_color")+msg.getString(path);
      string = string.replaceAll("%s", msg.getString("error_variable_color"));
      string = string.replaceAll("%n", msg.getString("error_color"));
      if (opt.length > 0) {
        for (int i = 0; i<opt.length; i++) {
          string = string.replaceAll("%"+(i+1), msg.getString("error_variable_color")+opt[i]+msg.getString("error_color"));
        }
      }
    } else if(path.startsWith("raw_")) {
      string = msg.getString(path);
    } else {
      string += msg.getString("message_color")+msg.getString(path);
      string = string.replaceAll("%s", msg.getString("message_variable_color"));
      string = string.replaceAll("%n", msg.getString("message_color"));
      if (opt.length > 0) {
        for (int i = 0; i<opt.length; i++) {
          string = string.replaceAll("%"+(i+1), msg.getString("message_variable_color")+opt[i]+msg.getString("message_color"));
        }
      }
    }
    return ChatColor.translateAlternateColorCodes('&', string);
  }
}
