package com.gmail.ZiomuuSs.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.ZiomuuSs.Nation.Group;
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
        if (!c.hasPermission("Nations.create", "Nations.*")) return true;
        if (args.length>2) {
          if (!c.isNotNation(args[1])) return true;
          if (!c.notMemberofNation(args[2])) return true;
          new Nation(config, args[1], args[2]);
          sender.sendMessage(msg.get("nation_created", true, args[1], args[2]));
        } else {
          sender.sendMessage(msg.get("error_usage", true, "/n create <name> <king>"));
        }
        break;
      case "rank":
        if (!c.hasNation()) return true;
        if (args.length>2 && !args[1].equalsIgnoreCase("add") && args[1].equalsIgnoreCase("list")) {
          Group group = Group.matchGroup(args[1], Nation.getPlayerNation(player));
          if (group == null) {
            sender.sendMessage(msg.get("error_group_not_exist", true, args[1]));
            return true;
          }
          switch (args[2].toLowerCase()) {
          case "perm":
            //todo
            break;
          case "del":
            //todo
            break;
          case "promote":
            //todo
            break;
          case "priority":
            //todo
            break;
          case "prefix":
            //todo
            break;
          default:
            sender.sendMessage(msg.get("error_usage", true, "/n rank <rank> <perm/del/promote/prefix/info/priority>"));
          }
        } else {
          switch (args[1].toLowerCase()) {
          case "list":
            String list = "";
            for (Group g : Group.getGroups()) {
              list+= g.toString()+", ";
            }
            if (!list.equalsIgnoreCase(""))
              list = list.substring(0, list.length() - 2);
            else
              list = msg.get("none", false);
            player.sendMessage(msg.get("nation_group_list", true, list));
            break;
          case "add":
            if (args.length>2) {
              Group group = Group.matchGroup(args[2], Nation.getPlayerNation(player));
              if (group == null) {
                new Group(args[2], Nation.getPlayerNation(player), config);
                player.sendMessage(msg.get("nation_group_created", true, args[2]));
                return true;
              } else {
                player.sendMessage(msg.get("error_group_exist", true, args[2]));
              }
            } else {
              sender.sendMessage(msg.get("error_usage", true, "/n rank add <name>"));
            }
            break;
          default:
            sender.sendMessage(msg.get("error_usage", true, "/n rank <rank> <list/add>"));
          }
        break;
        }
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