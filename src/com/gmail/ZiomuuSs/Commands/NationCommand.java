package com.gmail.ZiomuuSs.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Nation.Nation;
import com.gmail.ZiomuuSs.Utils.ConfigLoader;
import com.gmail.ZiomuuSs.Utils.msg;


public class NationCommand implements CommandExecutor {
  ConfigLoader config;
  
  public NationCommand(ConfigLoader config) {
    this.config = config;
  }
  
  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("Nation") || cmd.getName().equalsIgnoreCase("n")) {
      Player player;
      if (sender instanceof Player)
        player = (Player) sender;
      else {
        sender.sendMessage(msg.get("error_player_needed", true));
        return true;
      }
      Condition c = new Condition(player);
      if (args.length<1) sender.sendMessage(msg.get("error_usage", true, "/n help"));
      switch (args[0].toLowerCase()) {
      case "create":
        //todo
        if (!c.hasPermission("Nations.create", "Nations.*")) return true;
        if (args.length>1) {
          if (!c.isNotNation(args[1])) return true;
          new Nation(args[1], config);
        } else {
          sender.sendMessage(msg.get("error_usage", true, "/n create <name>"));
        }
        break;
      case "info":
        //todo
        break;
      case "join":
        //todo
        break;
      case "list":
        //todo
        break;
      default:
        sender.sendMessage(msg.get("error_usage", true, "/n help"));
      }
      return true;
    }
  return true;
  }
  
  
  
  
}