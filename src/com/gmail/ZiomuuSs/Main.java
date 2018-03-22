package com.gmail.ZiomuuSs;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.ZiomuuSs.Utils.ConfigLoader;
import com.gmail.ZiomuuSs.Utils.msg;

public final class Main extends JavaPlugin {
  private ConfigLoader config;
  
  public void onEnable() {
    reload(null);
  }
  
  public void onDisable() {
    //?
  }
  
  public void reload(CommandSender sender) {
    config = new ConfigLoader(this);
    if (sender != null) sender.sendMessage(msg.get("reloaded", true));
  }
  
}
